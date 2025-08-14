# 📺 Visual TV — Java-Based OTT Streaming Platform

**Visual TV** is a powerful desktop OTT application built in Java using JavaFX, Swing, Selenium, and VLCJ. It offers real-time access to trending movies, web series, and trailers with secure user authentication via SMTP. Designed for smooth playback, smart scraping, and personalized viewing.

---

## 🎬 Key Features

- 🔐 **User Authentication**
  - Email-based login with OTP verification via SMTP
  - Secure credential handling and session management

- 🌐 **Real-Time Scraping**
  - Uses Selenium to fetch trending content from IMDb
  - Scrapes movie ratings, cast, descriptions, and trailers

- 🎥 **Custom Media Player**
  - Built with VLCJ API
  - Supports resume, pause, forward, rewind, and progress bar
  - Separate players for movies and web series

- 📊 **Watch History & Resume**
  - Tracks recently watched content
  - Resumes playback from last watched timestamp using MySQL

- 🔍 **Search & Filter**
  - Search bar in all sections (Trending, Bollywood, Hollywood, Web Shows)
  - Filter by language, quality, and episode

- 🧾 **Content Sections**
  - Top Trending
  - Bollywood
  - Hollywood
  - Web Shows
  - Watch History
  - Favorites

---

## 🖼️ Visual Preview

### 🔐 Login Page

![Login Page](assets/v1.png)  
*Secure login with email and OTP verification.*

---

### 📝 Login Form

![Login Form](assets/v2.png)  
*Enter email and password to authenticate.*

---

### 🏠 Home Window

![Home Window](assets/v3.png)  
*Browse trending content and navigate sections.*

---

### 🎞️ Trailer Window

![Trailer Window](assets/v4.png)  
*Watch trailers in a custom VLCJ-powered player.*

---

### 🎬 Movie Window

![Movie Window](assets/v5.png)  
*Stream movies with full media controls.*

---

![Movie Window Alt](assets/v6.png)  
*Alternate view of movie playback.*

---

### 🔥 Top Trending

![Top Trending](assets/v7.png)  
*Explore the most popular shows and films.*

---

### 🎥 Hollywood Section

![Hollywood Section](assets/v8.png)  
*Browse trending Hollywood movies.*

---

### 📺 Web Shows Section

![Web Shows](assets/v9.png)  
*Stream web series from global platforms.*

---

## 🛠️ Tech Stack

| Layer       | Technology             |
|-------------|-------------------------|
| UI          | JavaFX, Swing           |
| Scraping    | Selenium WebDriver , Jsoup      |
| Media       | VLCJ (Java VLC API)     |
| Auth        | SMTP (JavaMail API)     |
| Database    | MySQL + MongoDB         |
| Automation  | Web Control + Browser Automation |

---

## 🚀 Getting Started

To run locally:

```bash
# Clone the repository
git clone https://github.com/abhi041540/VisualTV_JavaApp.git
cd VisualTV_JavaApp

# Compile and run
javac Main.java
java Main
