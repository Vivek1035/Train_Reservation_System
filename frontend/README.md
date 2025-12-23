# Train Reservation System - React Frontend

A modern, responsive React frontend for the Train Reservation System built with Vite, Tailwind CSS, and React Router.

## 🚀 Features

- **Authentication & Authorization**
  - JWT-based authentication
  - OAuth2 Google login
  - Protected routes
  - Role-based access control (User/Admin)

- **Train Booking**
  - Search trains by source, destination, and date
  - Interactive seat selection with visual coach layout
  - Real-time seat availability
  - Multiple coach types support

- **Payment Integration**
  - Dummy payment gateway simulation
  - Multiple payment methods (Credit/Debit Card, UPI, Net Banking, Wallet)
  - Payment retry mechanism
  - Test cards for different scenarios

- **User Features**
  - View booking history
  - Cancel bookings
  - Download tickets (PNR)
  - Profile management

- **Admin Dashboard**
  - Manage trains
  - Manage stations
  - View all bookings
  - User management

- **Responsive Design**
  - Mobile-first approach
  - Tailwind CSS styling
  - Dark mode support
  - Accessible components

## 📁 Project Structure

```
frontend/
├── public/
├── src/
│   ├── components/
│   │   ├── common/           # Reusable components
│   │   │   ├── Alert.jsx
│   │   │   ├── Badge.jsx
│   │   │   ├── Button.jsx
│   │   │   ├── Card.jsx
│   │   │   ├── Input.jsx
│   │   │   ├── Modal.jsx
│   │   │   └── Spinner.jsx
│   │   ├── layout/           # Layout components
│   │   │   ├── Navbar.jsx
│   │   │   └── Footer.jsx
│   │   └── ProtectedRoute.jsx
│   ├── contexts/
│   │   └── AuthContext.jsx   # Authentication context
│   ├── pages/
│   │   ├── admin/            # Admin pages
│   │   │   ├── AdminDashboard.jsx
│   │   │   └── ManageTrains.jsx
│   │   ├── auth/             # Authentication pages
│   │   │   ├── Login.jsx
│   │   │   ├── Register.jsx
│   │   │   └── OAuth2RedirectHandler.jsx
│   │   ├── bookings/         # Booking pages
│   │   │   └── MyBookings.jsx
│   │   ├── payment/          # Payment pages
│   │   │   └── Payment.jsx
│   │   ├── trains/           # Train search & booking
│   │   │   ├── SearchTrains.jsx
│   │   │   └── SeatSelection.jsx
│   │   └── Home.jsx
│   ├── services/             # API services
│   │   ├── api.js            # Axios instance with interceptors
│   │   ├── authService.js
│   │   ├── bookingService.js
│   │   ├── paymentService.js
│   │   ├── trainService.js
│   │   └── userService.js
│   ├── App.jsx               # Main app with routing
│   ├── main.jsx              # Entry point
│   └── index.css             # Global styles
├── index.html
├── package.json
├── vite.config.js
├── tailwind.config.js
└── postcss.config.js
```

## 🛠️ Tech Stack

- **React 18** - UI library
- **Vite** - Build tool and dev server
- **React Router 6** - Routing
- **Axios** - HTTP client
- **Tailwind CSS** - Styling
- **Lucide React** - Icons
- **date-fns** - Date utilities

## 📋 Prerequisites

- Node.js 18+ and npm/yarn
- Backend API running on `http://localhost:8080`

## 🚀 Getting Started

### 1. Install Dependencies

```bash
cd frontend
npm install
```

### 2. Environment Configuration

Create a `.env.local` file:

```env
VITE_API_BASE_URL=http://localhost:8080/api
```

### 3. Run Development Server

```bash
npm run dev
```

The app will be available at `http://localhost:5173`

### 4. Build for Production

```bash
npm run build
```

The build output will be in the `dist/` directory.

### 5. Preview Production Build

```bash
npm run preview
```

## 🔑 Authentication

### Login Credentials

**Admin User:**
- Email: admin@example.com
- Password: Admin@123

**Regular User:**
- Email: user@example.com
- Password: User@123

### OAuth2 Google Login

Configure Google OAuth2 credentials in the backend `application.properties`:

```properties
spring.security.oauth2.client.registration.google.client-id=YOUR_CLIENT_ID
spring.security.oauth2.client.registration.google.client-secret=YOUR_CLIENT_SECRET
```

## 💳 Payment Testing

### Test Card Numbers

The payment system uses a dummy gateway for testing:

- **Insufficient Funds:** Card ending in `0000` (e.g., `4532015112620000`)
- **Card Expired:** Card ending in `1111` (e.g., `4532015112621111`)
- **Invalid CVV:** Card ending in `2222` (e.g., `4532015112622222`)
- **Card Blocked:** Card ending in `3333` (e.g., `4532015112623333`)
- **Success (90% rate):** Any other card number

**Test Card Details:**
- Card Holder: Any name
- Expiry: Any future date
- CVV: Any 3 digits

## 🎨 UI Components

### Common Components

- **Button** - Primary, secondary, danger, outline, ghost variants
- **Input** - Text input with label and error handling
- **Card** - Container with shadow and hover effects
- **Alert** - Success, error, warning, info alerts
- **Badge** - Status badges
- **Modal** - Overlay dialogs
- **Spinner** - Loading indicators

### Usage Example

```jsx
import Button from './components/common/Button';
import Alert from './components/common/Alert';

function MyComponent() {
  return (
    <>
      <Button variant="primary" loading={isLoading}>
        Submit
      </Button>
      <Alert type="success" message="Action completed!" />
    </>
  );
}
```

## 🔒 Protected Routes

Routes are protected using the `ProtectedRoute` component:

```jsx
<Route
  path="/bookings"
  element={
    <ProtectedRoute>
      <MyBookings />
    </ProtectedRoute>
  }
/>

<Route
  path="/admin"
  element={
    <ProtectedRoute adminOnly>
      <AdminDashboard />
    </ProtectedRoute>
  }
/>
```

## 🌐 API Integration

### Service Layer

All API calls go through service modules:

```javascript
// Example: Booking a train
import { bookingService } from './services/bookingService';

const createBooking = async (bookingData) => {
  try {
    const response = await bookingService.createBooking(bookingData);
    console.log('Booking created:', response);
  } catch (error) {
    console.error('Booking failed:', error);
  }
};
```

### JWT Token Handling

Tokens are automatically attached to requests via Axios interceptors:

```javascript
// api.js
api.interceptors.request.use((config) => {
  const token = localStorage.getItem('accessToken');
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
});
```

Token refresh is handled automatically on 401 responses.

## 📱 Responsive Design

The application is fully responsive:

- **Mobile:** < 768px
- **Tablet:** 768px - 1024px
- **Desktop:** > 1024px

Tailwind breakpoints:
- `sm:` 640px
- `md:` 768px
- `lg:` 1024px
- `xl:` 1280px
- `2xl:` 1536px

## 🧪 Testing Payment Flow

1. **Search for trains** on the home page
2. **Select seats** from available coaches
3. **Proceed to payment**
4. Use test card numbers for different scenarios
5. **View booking** in "My Bookings"

## 🐛 Common Issues

### Issue: API calls fail with CORS error

**Solution:** Ensure backend CORS configuration allows `http://localhost:5173`:

```java
// SecurityConfig.java
.cors(cors -> cors.configurationSource(request -> {
    CorsConfiguration config = new CorsConfiguration();
    config.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
    // ...
}))
```

### Issue: OAuth2 redirect not working

**Solution:** Update redirect URI in Google Console and backend:

```properties
spring.security.oauth2.client.registration.google.redirect-uri={baseUrl}/login/oauth2/code/google
```

Frontend should handle redirect at `/oauth2/redirect?token=...`

### Issue: Tokens not persisting

**Solution:** Check browser localStorage and ensure tokens are being saved:

```javascript
localStorage.setItem('accessToken', token);
localStorage.setItem('refreshToken', refreshToken);
```

## 📝 Scripts

- `npm run dev` - Start development server
- `npm run build` - Build for production
- `npm run preview` - Preview production build
- `npm run lint` - Run ESLint

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License.

## 👥 Authors

- Train Reservation System Team

## 🙏 Acknowledgments

- React Team for the amazing library
- Tailwind CSS for the utility-first CSS framework
- Vite for the blazing-fast build tool
- Lucide for the beautiful icons
