import UserNavbar from "../components/UserNavbar"
import "../styles/User-browse.css"

const Browse = () => {
  return (
    <div className="browse-page">
      <UserNavbar />

      <div className="hero-section">
        <div className="hero-content">
          <h1 className="hero-title">Find on-demand help for your daily tasks anytime, anywhere with Tarabaho!</h1>
          <button className="become-trabahador-button">BECOME A TRABAHADOR!</button>
        </div>
      </div>

      <div className="browse-content">
        <button className="look-for-trabahador-button">LOOK FOR TRABAHADOR</button>

        <p className="browse-description">Browse Trabahador profiles and find the perfect match for your task.</p>

        <div className="service-categories">
          <div className="service-category">
            <div className="service-icon cleaning-icon">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path
                  d="M12 14C14.2091 14 16 12.2091 16 10C16 7.79086 14.2091 6 12 6C9.79086 6 8 7.79086 8 10C8 12.2091 9.79086 14 12 14Z"
                  stroke="#0078FF"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
                <path d="M16 10H20" stroke="#0078FF" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M4 10H8" stroke="#0078FF" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M12 6V2" stroke="#0078FF" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M12 18V14" stroke="#0078FF" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M16 14L18 16" stroke="#0078FF" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M8 14L6 16" stroke="#0078FF" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M16 6L18 4" stroke="#0078FF" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <path d="M8 6L6 4" stroke="#0078FF" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
              </svg>
            </div>
            <div className="service-name">CLEANING</div>
          </div>

          <div className="service-category">
            <div className="service-icon errands-icon">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path
                  d="M12 12H19M19 12L16 9M19 12L16 15"
                  stroke="#0078FF"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
                <path
                  d="M19 6V5C19 4.46957 18.7893 3.96086 18.4142 3.58579C18.0391 3.21071 17.5304 3 17 3H7C6.46957 3 5.96086 3.21071 5.58579 3.58579C5.21071 3.96086 5 4.46957 5 5V19C5 19.5304 5.21071 20.0391 5.58579 20.4142C5.96086 20.7893 6.46957 21 7 21H17C17.5304 21 18.0391 20.7893 18.4142 20.4142C18.7893 20.0391 19 19.5304 19 19V18"
                  stroke="#0078FF"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
              </svg>
            </div>
            <div className="service-name">ERRANDS</div>
          </div>

          <div className="service-category">
            <div className="service-icon tutoring-icon">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path
                  d="M12 4.35418C10.8954 3.52375 9.48 3 8 3C6.06 3 4.28 3.84 3 5.18V17.18C4.28 15.84 6.06 15 8 15C9.48 15 10.8954 15.5238 12 16.3542M12 4.35418C13.1046 3.52375 14.52 3 16 3C17.94 3 19.72 3.84 21 5.18V17.18C19.72 15.84 17.94 15 16 15C14.52 15 13.1046 15.5238 12 16.3542M12 4.35418V16.3542"
                  stroke="#0078FF"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
              </svg>
            </div>
            <div className="service-name">TUTORING</div>
          </div>

          <div className="service-category">
            <div className="service-icon babysitting-icon">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path
                  d="M9 10C9 10.7956 8.68393 11.5587 8.12132 12.1213C7.55871 12.6839 6.79565 13 6 13C5.20435 13 4.44129 12.6839 3.87868 12.1213C3.31607 11.5587 3 10.7956 3 10C3 9.20435 3.31607 8.44129 3.87868 7.87868C4.44129 7.31607 5.20435 7 6 7C6.79565 7 7.55871 7.31607 8.12132 7.87868C8.68393 8.44129 9 9.20435 9 10Z"
                  stroke="#0078FF"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
                <path
                  d="M21 10C21 10.7956 20.6839 11.5587 20.1213 12.1213C19.5587 12.6839 18.7956 13 18 13C17.2044 13 16.4413 12.6839 15.8787 12.1213C15.3161 11.5587 15 10.7956 15 10C15 9.20435 15.3161 8.44129 15.8787 7.87868C16.4413 7.31607 17.2044 7 18 7C18.7956 7 19.5587 7.31607 20.1213 7.87868C20.6839 8.44129 21 9.20435 21 10Z"
                  stroke="#0078FF"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
                <path
                  d="M6 13V15C6 16.0609 6.42143 17.0783 7.17157 17.8284C7.92172 18.5786 8.93913 19 10 19H14C15.0609 19 16.0783 18.5786 16.8284 17.8284C17.5786 17.0783 18 16.0609 18 15V13"
                  stroke="#0078FF"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
              </svg>
            </div>
            <div className="service-name">BABYSITTING</div>
          </div>

          <div className="service-category">
            <div className="service-icon gardening-icon">
              <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path
                  d="M12 19V12M12 12C13.6569 12 15 10.6569 15 9C15 7.34315 13.6569 6 12 6C10.3431 6 9 7.34315 9 9C9 10.6569 10.3431 12 12 12Z"
                  stroke="#0078FF"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
                <path d="M9 17H15" stroke="#0078FF" strokeWidth="2" strokeLinecap="round" strokeLinejoin="round" />
                <path
                  d="M3 13C3 17.9706 7.02944 22 12 22C16.9706 22 21 17.9706 21 13C21 8.02944 16.9706 4 12 4C7.02944 4 3 8.02944 3 13Z"
                  stroke="#0078FF"
                  strokeWidth="2"
                  strokeLinecap="round"
                  strokeLinejoin="round"
                />
              </svg>
            </div>
            <div className="service-name">GARDENING</div>
          </div>
        </div>
      </div>
    </div>
  )
}

export default Browse
