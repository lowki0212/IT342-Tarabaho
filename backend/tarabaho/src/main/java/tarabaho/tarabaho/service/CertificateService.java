package tarabaho.tarabaho.service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import tarabaho.tarabaho.entity.Certificate;
import tarabaho.tarabaho.entity.Worker;
import tarabaho.tarabaho.repository.CertificateRepository;
import tarabaho.tarabaho.repository.WorkerRepository;

@Service
public class CertificateService {

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private WorkerRepository workerRepository;

    public Certificate addCertificate(
            Long workerId,
            String courseName,
            String certificateNumber,
            String issueDate,
            MultipartFile certificateFile
    ) throws Exception {
        System.out.println("CertificateService: Adding certificate for worker ID: " + workerId);
        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new Exception("Worker not found with id: " + workerId));

        Certificate certificate = new Certificate();
        certificate.setCourseName(courseName);
        certificate.setCertificateNumber(certificateNumber);
        certificate.setIssueDate(issueDate);
        certificate.setWorker(worker);

        if (certificateFile != null && !certificateFile.isEmpty()) {
            String contentType = certificateFile.getContentType();
            if (!contentType.startsWith("image/") && !contentType.equals("application/pdf")) {
                throw new Exception("Only image or PDF files are allowed.");
            }

            String uploadDir = "uploads/certificates/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (!created) {
                    throw new Exception("Failed to create upload directory: " + uploadDir);
                }
            }

            if (!directory.canWrite()) {
                throw new Exception("Upload directory is not writable: " + uploadDir);
            }

            String fileName = UUID.randomUUID().toString() + "_" + certificateFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName).toAbsolutePath().normalize();
            System.out.println("CertificateService: Saving file to: " + filePath.toString());
            Files.write(filePath, certificateFile.getBytes());

            certificate.setCertificateFilePath("/certificates/" + fileName);
        }

        Certificate savedCertificate = certificateRepository.save(certificate);
        System.out.println("CertificateService: Certificate saved, ID: " + savedCertificate.getId());
        return savedCertificate;
    }

    public Certificate updateCertificate(
            Long certificateId,
            Long workerId,
            String courseName,
            String certificateNumber,
            String issueDate,
            MultipartFile certificateFile
    ) throws Exception {
        System.out.println("CertificateService: Updating certificate ID: " + certificateId);
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new Exception("Certificate not found with id: " + certificateId));

        Worker worker = workerRepository.findById(workerId)
                .orElseThrow(() -> new Exception("Worker not found with id: " + workerId));

        certificate.setCourseName(courseName);
        certificate.setCertificateNumber(certificateNumber);
        certificate.setIssueDate(issueDate);
        certificate.setWorker(worker);

        if (certificateFile != null && !certificateFile.isEmpty()) {
            String contentType = certificateFile.getContentType();
            if (!contentType.startsWith("image/") && !contentType.equals("application/pdf")) {
                throw new Exception("Only image or PDF files are allowed.");
            }

            String uploadDir = "uploads/certificates/";
            File directory = new File(uploadDir);
            if (!directory.exists()) {
                boolean created = directory.mkdirs();
                if (!created) {
                    throw new Exception("Failed to create upload directory: " + uploadDir);
                }
            }

            if (!directory.canWrite()) {
                throw new Exception("Upload directory is not writable: " + uploadDir);
            }

            // Delete old file if exists
            if (certificate.getCertificateFilePath() != null) {
                Path oldFilePath = Paths.get("uploads" + certificate.getCertificateFilePath()).toAbsolutePath().normalize();
                Files.deleteIfExists(oldFilePath);
            }

            String fileName = UUID.randomUUID().toString() + "_" + certificateFile.getOriginalFilename();
            Path filePath = Paths.get(uploadDir, fileName).toAbsolutePath().normalize();
            System.out.println("CertificateService: Saving file to: " + filePath.toString());
            Files.write(filePath, certificateFile.getBytes());

            certificate.setCertificateFilePath("/certificates/" + fileName);
        }

        Certificate updatedCertificate = certificateRepository.save(certificate);
        System.out.println("CertificateService: Certificate updated, ID: " + updatedCertificate.getId());
        return updatedCertificate;
    }

    public void deleteCertificate(Long certificateId) throws Exception {
        System.out.println("CertificateService: Deleting certificate ID: " + certificateId);
        Certificate certificate = certificateRepository.findById(certificateId)
                .orElseThrow(() -> new Exception("Certificate not found with id: " + certificateId));

        // Delete file from filesystem
        if (certificate.getCertificateFilePath() != null) {
            Path filePath = Paths.get("uploads" + certificate.getCertificateFilePath()).toAbsolutePath().normalize();
            Files.deleteIfExists(filePath);
        }

        certificateRepository.deleteById(certificateId);
        System.out.println("CertificateService: Certificate deleted, ID: " + certificateId);
    }

    public List<Certificate> getCertificatesByWorkerId(Long workerId) {
        System.out.println("CertificateService: Fetching certificates for worker ID: " + workerId);
        List<Certificate> certificates = certificateRepository.findByWorkerId(workerId);
        System.out.println("CertificateService: Retrieved " + certificates.size() + " certificates");
        return certificates;
    }

    public Optional<Certificate> getCertificateById(Long certificateId) {
        System.out.println("CertificateService: Fetching certificate ID: " + certificateId);
        Optional<Certificate> certificate = certificateRepository.findById(certificateId);
        if (certificate.isPresent()) {
            System.out.println("CertificateService: Certificate found, ID: " + certificateId);
        } else {
            System.out.println("CertificateService: Certificate not found, ID: " + certificateId);
        }
        return certificate;
    }
}