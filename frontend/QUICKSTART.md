# Train Reservation System - Quick Start Guide

## 🚀 Quick Setup (5 Minutes)

### 1. Install Dependencies
```bash
cd frontend
npm install
```

### 2. Configure Environment
```bash
# Create .env.local file
echo "VITE_API_BASE_URL=http://localhost:8080/api" > .env.local
```

### 3. Start Development Server
```bash
npm run dev
```

Visit: http://localhost:5173

## 📋 Project Structure (Simplified)

```
frontend/
├── src/
│   ├── services/         # API calls (6 files)
│   ├── components/       # UI components (10 files)
│   ├── contexts/         # Auth state (1 file)
│   ├── pages/           # Application pages (12 files)
│   └── App.jsx          # Routes & layout
├── package.json
├── vite.config.js
└── tailwind.config.js
```

## 🔑 Test Accounts

**Admin:**
- Email: `admin@example.com`
- Password: `Admin@123`

**User:**
- Email: `user@example.com`
- Password: `User@123`

## 💳 Test Cards

**Success (90%):** Any 16-digit card (e.g., `4532015112620001`)
**Insufficient Funds:** `4532015112620000`
**Card Expired:** `4532015112621111`
**Invalid CVV:** `4532015112622222`
**Card Blocked:** `4532015112623333`

## 🎯 Main Routes

| Route | Description | Auth Required | Admin Only |
|-------|-------------|---------------|------------|
| `/` | Home page | No | No |
| `/login` | Login page | No | No |
| `/register` | Registration | No | No |
| `/search` | Search trains | Yes | No |
| `/trains/:id/seats` | Seat selection | Yes | No |
| `/payment` | Payment page | Yes | No |
| `/bookings` | My bookings | Yes | No |
| `/admin` | Admin dashboard | Yes | Yes |
| `/admin/trains` | Manage trains | Yes | Yes |

## 📦 Key Dependencies

```json
{
  "react": "^18.2.0",
  "react-router-dom": "^6.22.0",
  "axios": "^1.6.7",
  "tailwindcss": "^3.4.1",
  "lucide-react": "^0.315.0",
  "date-fns": "^3.3.1"
}
```

## 🛠️ Available Scripts

```bash
npm run dev      # Start dev server (port 5173)
npm run build    # Build for production
npm run preview  # Preview production build
npm run lint     # Run ESLint
```

## 🔧 Common Tasks

### Add New Page
1. Create component in `src/pages/`
2. Add route in `App.jsx`
3. Add navigation link in `Navbar.jsx`

### Add New API Service
1. Create service in `src/services/`
2. Import and use in components
3. Handle errors with try-catch

### Create Reusable Component
1. Add to `src/components/common/`
2. Export default
3. Import in pages

## 🎨 Styling Guide

### Using Tailwind Classes
```jsx
<div className="bg-white rounded-lg shadow-md p-6 hover:shadow-lg">
  <h2 className="text-xl font-bold text-gray-900 mb-4">Title</h2>
  <p className="text-gray-600">Content</p>
</div>
```

### Using Custom Components
```jsx
<Card hover>
  <Button variant="primary" loading={isLoading}>
    Submit
  </Button>
</Card>
```

## 🔒 Authentication Usage

### In Components
```jsx
import { useAuth } from './contexts/AuthContext';

function MyComponent() {
  const { user, isAuthenticated, logout } = useAuth();
  
  return (
    <div>
      {isAuthenticated ? (
        <p>Welcome, {user.fullName}</p>
      ) : (
        <p>Please login</p>
      )}
    </div>
  );
}
```

### Protected Routes
```jsx
<Route
  path="/protected"
  element={
    <ProtectedRoute>
      <ProtectedPage />
    </ProtectedRoute>
  }
/>
```

## 🌐 API Calls

### Using Services
```jsx
import { trainService } from './services/trainService';

const fetchTrains = async () => {
  try {
    const trains = await trainService.searchTrains({
      sourceStationId: 1,
      destinationStationId: 2,
      journeyDate: '2025-01-01'
    });
    setTrains(trains);
  } catch (error) {
    console.error('Error:', error);
  }
};
```

## 🐛 Troubleshooting

**Issue: CORS Error**
```bash
# Solution: Check backend CORS config allows http://localhost:5173
```

**Issue: 401 Unauthorized**
```bash
# Solution: Check token in localStorage, login again if expired
localStorage.getItem('accessToken')
```

**Issue: Module not found**
```bash
# Solution: Reinstall dependencies
rm -rf node_modules package-lock.json
npm install
```

**Issue: Port 5173 in use**
```bash
# Solution: Kill process or use different port
vite --port 3000
```

## 📱 Mobile Testing

```bash
# Find your local IP
ipconfig  # Windows
ifconfig  # Mac/Linux

# Access from mobile
http://YOUR_IP:5173
```

## 🚢 Production Deployment

### Build
```bash
npm run build
# Output: dist/
```

### Deploy to Netlify
```bash
# Add netlify.toml
[build]
  command = "npm run build"
  publish = "dist"

[[redirects]]
  from = "/*"
  to = "/index.html"
  status = 200
```

### Deploy to Vercel
```bash
vercel --prod
# Automatically detects Vite
```

## 📊 File Count

- **Total Files:** 40+
- **React Components:** 22
- **Service Files:** 6
- **Pages:** 12
- **Common Components:** 7

## 🎓 Learning Path

1. **Start:** Home.jsx, App.jsx
2. **Auth:** Login.jsx, AuthContext.jsx, authService.js
3. **Booking:** SearchTrains.jsx, SeatSelection.jsx
4. **Payment:** Payment.jsx, paymentService.js
5. **Admin:** AdminDashboard.jsx, ManageTrains.jsx

## 📞 Need Help?

1. Check `README.md` for detailed docs
2. Review `IMPLEMENTATION_SUMMARY.md` for architecture
3. Check browser console for errors
4. Verify backend is running on port 8080

## ✅ Pre-Launch Checklist

- [ ] Backend API running
- [ ] Environment variables set
- [ ] Dependencies installed
- [ ] Dev server starts successfully
- [ ] Login works
- [ ] Train search works
- [ ] Seat selection works
- [ ] Payment simulation works
- [ ] Booking confirmation works
- [ ] Admin dashboard accessible

---

**Ready to code?** Start with: `npm run dev` 🚀
