# 🛡️ Child Safety Monitoring System

A comprehensive parental control and child safety monitoring system with two mobile apps and a web dashboard.

## 📱 Components

### 1. Child Monitoring App (`child-safety-apk/`)
**Install on child's device**

Features:
- ✅ Real-time location tracking
- ✅ Screen time monitoring
- ✅ App usage tracking
- ✅ Call & SMS monitoring
- ✅ Web activity tracking
- ✅ Remote screenshots
- ✅ Background service (always running)
- ✅ Links to parent via 6-digit code

**Installation:**
1. Build APK from `child-safety-apk/` folder
2. Install on child's device
3. Complete setup wizard
4. Enter parent's linking code
5. Grant all required permissions

**Package:** `com.childsafety.monitor`

---

### 2. Parent Control App (`parent-control-apk/`)
**Install on parent's device**

Features:
- ✅ Monitor multiple children
- ✅ View real-time location on map
- ✅ Check app usage statistics
- ✅ Set screen time limits
- ✅ Block/unblock apps remotely
- ✅ View activity timeline
- ✅ Receive safety alerts
- ✅ Content filtering controls
- ✅ Geofence alerts
- ✅ Remote device lock

**Installation:**
1. Build APK from `parent-control-apk/` folder
2. Install on parent's device
3. Open app to get 6-digit linking code
4. Share code with child device during setup

**Package:** `com.childsafety.parent`

---

### 3. Parent Web Dashboard (`parent-dashboard.html`)
**Access from any browser**

Features:
- ✅ Monitor all children from web browser
- ✅ Live location tracking on map
- ✅ App usage statistics
- ✅ Activity timeline
- ✅ Quick actions (screenshot, lock, etc.)
- ✅ Screen time controls
- ✅ Content filters
- ✅ Safety alerts

**Access:**
Open `http://your-server-url/parent-dashboard` in browser

---

### 4. Backend Server (`server code v3/`)
**Node.js WebSocket server**

New Features Added:
- ✅ Parent-child device linking
- ✅ Real-time data sync
- ✅ Child safety API endpoints
- ✅ Location tracking
- ✅ App usage data storage
- ✅ Command relay system

**API Endpoints:**

```javascript
POST /api/child-safety/register-parent
POST /api/child-safety/register-child
GET  /api/child-safety/children/:parentId
POST /api/child-safety/command
GET  /api/child-safety/location/:deviceId
GET  /api/child-safety/app-usage/:deviceId
GET  /parent-dashboard
```

---

## 🚀 Quick Start

### Step 1: Setup Backend Server

```bash
cd "APPS&Soucrce-Code/source code/server code v3"
npm install
node index.js
```

Server will start on port 8000 (or your configured port)

### Step 2: Build Child Monitoring App

```bash
cd "APPS&Soucrce-Code/source code/child-safety-apk"
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

### Step 3: Build Parent Control App

```bash
cd "APPS&Soucrce-Code/source code/parent-control-apk"
./gradlew assembleRelease
```

Output: `app/build/outputs/apk/release/app-release.apk`

### Step 4: Deploy

1. **Install Child App** on child's device
2. **Install Parent App** on parent's device
3. **Open Parent App** → Get linking code
4. **Open Child App** → Complete setup → Enter parent's code
5. **Access Web Dashboard** at `http://your-server/parent-dashboard`

---

## 🔐 Security Features

- ✅ Encrypted WebSocket communication
- ✅ Unique device IDs
- ✅ Parent-child code verification
- ✅ Secure command relay
- ✅ Permission-based access
- ✅ Background service protection

---

## 📊 Monitoring Features

### Location Tracking
- Real-time GPS location
- Location history
- Geofence alerts
- Safe zones

### App Monitoring
- App usage time
- Most used apps
- App blocking capability
- Installation alerts

### Screen Time
- Daily screen time
- Weekly reports
- Time limits
- Bedtime schedules

### Activity Monitoring
- Call logs
- SMS messages
- Web browsing history
- Notification tracking

### Safety Controls
- Content filtering
- Safe search enforcement
- Social media restrictions
- Emergency SOS button

---

## 🎨 User Interface

### Child App UI
- Simple, minimal interface
- Shows monitoring status
- Active features list
- Cannot be easily closed

### Parent App UI
- Material Design 3
- Bottom navigation
- Detailed statistics
- Quick action buttons
- Beautiful charts and graphs

### Web Dashboard UI
- Responsive design
- Real-time updates
- Interactive charts
- Multi-device support

---

## 🔧 Configuration

### Server Configuration
Edit `server code v3/index.js`:
```javascript
const token = 'YOUR_TELEGRAM_BOT_TOKEN'
const id = 'YOUR_TELEGRAM_CHAT_ID'
```

### Child App Configuration
Edit `child-safety-apk/app/src/main/java/.../utils/PreferenceManager.kt`:
- Set default server URL
- Configure sync intervals

### Parent App Configuration
Edit `parent-control-apk/app/src/main/java/.../utils/PreferenceManager.kt`:
- Set default server URL
- Configure update intervals

---

## 📱 System Requirements

### Child Device
- Android 7.0+ (API 24+)
- 2GB RAM minimum
- GPS enabled
- Internet connection

### Parent Device
- Android 7.0+ (API 24+)
- 1GB RAM minimum
- Internet connection

### Server
- Node.js 14+
- 512MB RAM minimum
- Port 8000 open
- WebSocket support

---

## 🛠️ Development

### Technologies Used

**Mobile Apps:**
- Kotlin
- Jetpack Compose
- Material Design 3
- WebSocket Client
- Coroutines
- Room Database

**Backend:**
- Node.js
- Express
- WebSocket (ws)
- Telegram Bot API

**Web Dashboard:**
- HTML5
- CSS3
- JavaScript (Vanilla)
- WebSocket API

---

## 📝 License

This is a monitoring and parental control system. Use responsibly and ensure compliance with local laws regarding child monitoring and privacy.

---

## ⚠️ Important Notes

1. **Legal Compliance**: Ensure you have legal authority to monitor the device
2. **Privacy**: Only use for legitimate parental control purposes
3. **Permissions**: All required permissions must be granted for full functionality
4. **Battery**: Monitoring may impact battery life
5. **Data**: Requires active internet connection for real-time features

---

## 🆘 Support

For issues or questions:
1. Check the logs in `uploadedFile/` folder
2. Monitor Telegram bot messages
3. Check browser console for web dashboard issues
4. Review Android logcat for app issues

---

## 🎯 Future Enhancements

- [ ] AI-powered content detection
- [ ] Advanced geofencing
- [ ] Social media monitoring
- [ ] Screen recording
- [ ] Multi-language support
- [ ] iOS version
- [ ] Cloud backup
- [ ] Parent community features

---

## 📞 Contact

For support or inquiries, please refer to the documentation or check the logs.

---

**Made with ❤️ for child safety**
