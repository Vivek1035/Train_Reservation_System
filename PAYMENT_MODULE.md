# 💳 Payment Module Documentation

## Overview
This is a **DUMMY PAYMENT MODULE** for the Train Reservation System. It simulates payment gateway interactions without integrating with real payment processors. Perfect for development, testing, and demonstrations.

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                  Payment Architecture                    │
├─────────────────────────────────────────────────────────┤
│                                                           │
│  ┌──────────┐         ┌─────────────────┐               │
│  │  Client  │────────►│ PaymentController│               │
│  │ (React)  │         │  - /process      │               │
│  └──────────┘         │  - /retry        │               │
│                       │  - /refund       │               │
│                       └────────┬─────────┘               │
│                                │                         │
│                                ▼                         │
│                       ┌─────────────────┐               │
│                       │ PaymentService   │               │
│                       │ - Validation     │               │
│                       │ - Simulation     │               │
│                       │ - Persistence    │               │
│                       └────────┬─────────┘               │
│                                │                         │
│                  ┌─────────────┴──────────────┐         │
│                  ▼                            ▼         │
│         ┌────────────────┐          ┌────────────────┐ │
│         │PaymentRepository│          │BookingRepository│ │
│         │  - Save payment │          │  - Update status│ │
│         └────────┬────────┘          └────────┬────────┘ │
│                  │                            │         │
│                  ▼                            ▼         │
│         ┌────────────────────────────────────────────┐ │
│         │            PostgreSQL Database              │ │
│         │   payments table  |  bookings table        │ │
│         └────────────────────────────────────────────┘ │
└─────────────────────────────────────────────────────────┘
```

---

## 💰 Payment Lifecycle

### 1. Payment Initiation Flow

```
┌──────────┐         ┌────────────┐         ┌──────────┐
│  Client  │         │  Backend   │         │ Database │
└────┬─────┘         └──────┬─────┘         └────┬─────┘
     │                      │                     │
     │ 1. Create Booking    │                     │
     ├─────────────────────►│                     │
     │                      │ Save booking        │
     │                      │ (status: PENDING)   │
     │                      ├────────────────────►│
     │                      │◄────────────────────┤
     │                      │                     │
     │ 2. Booking created   │                     │
     │    PNR: 1234567890   │                     │
     │◄─────────────────────┤                     │
     │                      │                     │
     │ 3. Process Payment   │                     │
     │    {bookingId,       │                     │
     │     amount,          │                     │
     │     cardDetails}     │                     │
     ├─────────────────────►│                     │
     │                      │                     │
     │                      │ 4. Validate booking │
     │                      │    and amount       │
     │                      │                     │
     │                      │ 5. Simulate gateway │
     │                      │    (1-3 sec delay)  │
     │                      │                     │
     │                      │ 6. Save payment     │
     │                      ├────────────────────►│
     │                      │◄────────────────────┤
     │                      │                     │
     │                      │ 7. Update booking   │
     │                      │    status: CONFIRMED│
     │                      ├────────────────────►│
     │                      │◄────────────────────┤
     │                      │                     │
     │ 8. Payment SUCCESS   │                     │
     │    TXN123456...      │                     │
     │◄─────────────────────┤                     │
     │                      │                     │
```

### 2. Payment Success Path

```
Booking Created (PENDING)
         │
         ▼
Payment Request Received
         │
         ▼
Validation Passed
         │
         ▼
Simulate Gateway Processing
         │
         ▼
SUCCESS (90% probability)
         │
         ├──► Generate Transaction ID
         │
         ├──► Save Payment (status: SUCCESS)
         │
         ├──► Update Booking (status: CONFIRMED)
         │
         └──► Return PaymentResponse
                   - transactionId: TXN170317...
                   - status: SUCCESS
                   - canRetry: false
```

### 3. Payment Failure Path

```
Booking Created (PENDING)
         │
         ▼
Payment Request Received
         │
         ▼
Validation Passed
         │
         ▼
Simulate Gateway Processing
         │
         ▼
FAILURE (10% probability or test cards)
         │
         ├──► Determine failure reason:
         │    • Insufficient funds
         │    • Card expired
         │    • Invalid CVV
         │    • Card blocked
         │    • Network error
         │
         ├──► Save Payment (status: FAILED)
         │
         ├──► Keep Booking (status: PENDING)
         │
         └──► Return PaymentResponse
                   - transactionId: TXN170317...
                   - status: FAILED
                   - statusMessage: "Payment failed: ..."
                   - canRetry: true
```

### 4. Payment Retry Flow

```
Failed Payment (FAILED)
         │
         ▼
User clicks "Retry Payment"
         │
         ▼
New Payment Request
         │
         ▼
Validate original payment
  (must be FAILED status)
         │
         ▼
Process as new payment
         │
         ├──► SUCCESS ──► Update booking to CONFIRMED
         │
         └──► FAILED ──► Keep booking PENDING
```

### 5. Refund Flow

```
Booking Cancelled by User
         │
         ▼
Find Payment for Booking
         │
         ▼
Validate Payment Status
  (must be SUCCESS)
         │
         ▼
Simulate Refund Processing
         │
         ├──► Update Payment (status: REFUNDED)
         │
         ├──► Add refund timestamp
         │
         └──► Return refund confirmation
```

---

## 🎯 Payment Simulation Logic

### Success Scenarios

**90% Success Rate** - Normal cards process successfully

**Test Cards for SUCCESS:**
- Any card ending in `4567`, `5678`, `6789`, `7890`, etc.
- Example: `4111 1111 1111 1234`

### Failure Scenarios

**Test Cards for FAILURE:**

| Card Number Ending | Failure Reason | HTTP Status |
|-------------------|----------------|-------------|
| `0000` | Insufficient funds | 402 |
| `1111` | Card expired | 402 |
| `2222` | Invalid CVV | 402 |
| `3333` | Card blocked by issuer | 402 |

**Random Failures (10% of requests):**
- Transaction timeout
- Card declined
- Network error
- Daily limit exceeded

### Force Failure (Testing)
```json
{
  "forceFailure": true
}
```
Guarantees payment failure for testing error handling.

---

## 📡 API Endpoints

### 1. Process Payment

**Endpoint:** `POST /api/payments/process`

**Description:** Process payment for a booking

**Request:**
```json
{
  "bookingId": 1,
  "amount": 9000.00,
  "paymentMethod": "CREDIT_CARD",
  "cardNumber": "4111111111111234",
  "cardHolderName": "John Doe",
  "expiryMonth": "12",
  "expiryYear": "2027",
  "cvv": "123",
  "forceFailure": false
}
```

**Success Response (201):**
```json
{
  "success": true,
  "message": "Payment processed successfully",
  "data": {
    "id": 1,
    "bookingId": 1,
    "pnrNumber": "1234567890",
    "transactionId": "TXN1703174400ABC123",
    "amount": 9000.00,
    "paymentMethod": "CREDIT_CARD",
    "status": "SUCCESS",
    "statusMessage": "Payment completed successfully",
    "paymentDate": "2025-12-21T15:30:00",
    "createdAt": "2025-12-21T15:30:00",
    "maskedCardNumber": "****",
    "canRetry": false
  }
}
```

**Failure Response (402):**
```json
{
  "success": true,
  "message": "Payment failed. Please try again.",
  "data": {
    "id": 2,
    "bookingId": 1,
    "pnrNumber": "1234567890",
    "transactionId": "TXN1703174401XYZ456",
    "amount": 9000.00,
    "paymentMethod": "CREDIT_CARD",
    "status": "FAILED",
    "statusMessage": "Payment failed: Insufficient funds",
    "paymentDate": null,
    "createdAt": "2025-12-21T15:31:00",
    "maskedCardNumber": "****",
    "canRetry": true
  }
}
```

**Validation Errors (400):**
```json
{
  "timestamp": "2025-12-21T15:32:00",
  "status": 400,
  "error": "Validation Failed",
  "message": "Input validation error",
  "details": [
    "amount: Amount must be greater than 0",
    "cardNumber: Card number is required"
  ]
}
```

**cURL:**
```bash
curl -X POST http://localhost:8080/api/payments/process \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_JWT_TOKEN" \
  -d '{
    "bookingId": 1,
    "amount": 9000.00,
    "paymentMethod": "CREDIT_CARD",
    "cardNumber": "4111111111111234",
    "cardHolderName": "John Doe",
    "expiryMonth": "12",
    "expiryYear": "2027",
    "cvv": "123"
  }'
```

---

### 2. Retry Failed Payment

**Endpoint:** `POST /api/payments/{paymentId}/retry`

**Description:** Retry a failed payment

**Request:**
```json
{
  "bookingId": 1,
  "amount": 9000.00,
  "paymentMethod": "DEBIT_CARD",
  "cardNumber": "5555555555554444",
  "cardHolderName": "John Doe",
  "expiryMonth": "12",
  "expiryYear": "2027",
  "cvv": "456"
}
```

**Response (200):**
```json
{
  "success": true,
  "message": "Payment retry successful",
  "data": {
    "id": 3,
    "status": "SUCCESS",
    "transactionId": "TXN1703174402NEW789",
    ...
  }
}
```

---

### 3. Get Payment by Transaction ID

**Endpoint:** `GET /api/payments/transaction/{transactionId}`

**Response:**
```json
{
  "success": true,
  "message": "Request processed successfully",
  "data": {
    "id": 1,
    "transactionId": "TXN1703174400ABC123",
    "status": "SUCCESS",
    ...
  }
}
```

---

### 4. Get Payment by Booking ID

**Endpoint:** `GET /api/payments/booking/{bookingId}`

**Response:**
```json
{
  "success": true,
  "message": "Request processed successfully",
  "data": {
    "id": 1,
    "bookingId": 1,
    "status": "SUCCESS",
    ...
  }
}
```

---

### 5. Get User Payment History

**Endpoint:** `GET /api/payments/user/{userId}`

**Response:**
```json
{
  "success": true,
  "message": "Found 3 payment(s)",
  "data": [
    {
      "id": 1,
      "status": "SUCCESS",
      "amount": 9000.00,
      ...
    },
    {
      "id": 2,
      "status": "FAILED",
      "amount": 3000.00,
      ...
    },
    {
      "id": 3,
      "status": "REFUNDED",
      "amount": 4500.00,
      ...
    }
  ]
}
```

---

### 6. Refund Payment

**Endpoint:** `POST /api/payments/{paymentId}/refund`

**Description:** Refund a successful payment (for cancelled bookings)

**Response (200):**
```json
{
  "success": true,
  "message": "Refund processed successfully",
  "data": {
    "id": 1,
    "status": "REFUNDED",
    "statusMessage": "Refund processed successfully",
    ...
  }
}
```

---

## 🗄️ Database Schema

### payments Table

```sql
CREATE TABLE payments (
    id BIGSERIAL PRIMARY KEY,
    booking_id BIGINT NOT NULL UNIQUE,
    transaction_id VARCHAR(255) NOT NULL UNIQUE,
    amount DECIMAL(10,2) NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    status VARCHAR(50) NOT NULL,
    payment_gateway_response TEXT,
    payment_date TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMP NOT NULL DEFAULT NOW(),
    FOREIGN KEY (booking_id) REFERENCES bookings(id)
);

CREATE INDEX idx_payment_booking ON payments(booking_id);
CREATE INDEX idx_payment_transaction ON payments(transaction_id);
```

### Payment Statuses

| Status | Description |
|--------|-------------|
| `SUCCESS` | Payment completed successfully |
| `FAILED` | Payment failed, can retry |
| `PENDING` | Payment processing (not used in dummy) |
| `REFUNDED` | Payment refunded after cancellation |

### Payment Methods

| Method | Description |
|--------|-------------|
| `CREDIT_CARD` | Credit card payment |
| `DEBIT_CARD` | Debit card payment |
| `UPI` | UPI payment |
| `NET_BANKING` | Net banking |
| `WALLET` | Digital wallet |

---

## 🔐 Security Considerations

### ⚠️ Important: This is a DUMMY Implementation

**NEVER use this in production without:**

1. **Real Payment Gateway Integration**
   - Razorpay, Stripe, PayPal, etc.
   - PCI DSS compliance
   - Tokenization

2. **Card Data Handling**
   - Currently, card details are NOT stored
   - In production, use payment gateway tokens
   - Never store CVV

3. **HTTPS/SSL**
   - All payment traffic must be encrypted
   - Use SSL certificates

4. **Payment Verification**
   - Webhook verification
   - Signature validation
   - Idempotency keys

5. **Fraud Prevention**
   - 3D Secure authentication
   - Address verification
   - Velocity checks
   - Risk scoring

### Current Security Features

✅ Card details NOT persisted to database  
✅ Transaction ID generation  
✅ Amount validation  
✅ Booking status validation  
✅ Authentication required  
✅ Transaction audit trail  

---

## 🧪 Testing Guide

### Test Scenario 1: Successful Payment

```bash
# 1. Create booking
curl -X POST http://localhost:8080/api/bookings \
  -H "Authorization: Bearer TOKEN" \
  -d '{...booking details...}'

# 2. Process payment with valid card
curl -X POST http://localhost:8080/api/payments/process \
  -H "Authorization: Bearer TOKEN" \
  -d '{
    "bookingId": 1,
    "amount": 9000.00,
    "paymentMethod": "CREDIT_CARD",
    "cardNumber": "4111111111111234",
    "cardHolderName": "Test User",
    "expiryMonth": "12",
    "expiryYear": "2027",
    "cvv": "123"
  }'

# Expected: 201 Created, status: SUCCESS, booking confirmed
```

### Test Scenario 2: Failed Payment (Insufficient Funds)

```bash
# Use card ending in 0000
curl -X POST http://localhost:8080/api/payments/process \
  -H "Authorization: Bearer TOKEN" \
  -d '{
    "bookingId": 1,
    "amount": 9000.00,
    "paymentMethod": "CREDIT_CARD",
    "cardNumber": "4111111111110000",
    ...
  }'

# Expected: 402 Payment Required, status: FAILED, canRetry: true
```

### Test Scenario 3: Retry Failed Payment

```bash
# After failed payment, retry with valid card
curl -X POST http://localhost:8080/api/payments/2/retry \
  -H "Authorization: Bearer TOKEN" \
  -d '{
    "bookingId": 1,
    "amount": 9000.00,
    "paymentMethod": "CREDIT_CARD",
    "cardNumber": "4111111111111234",
    ...
  }'

# Expected: 200 OK, status: SUCCESS
```

### Test Scenario 4: Refund

```bash
# 1. Cancel booking
curl -X PATCH http://localhost:8080/api/bookings/1/cancel \
  -H "Authorization: Bearer TOKEN"

# 2. Process refund
curl -X POST http://localhost:8080/api/payments/1/refund \
  -H "Authorization: Bearer TOKEN"

# Expected: 200 OK, status: REFUNDED
```

---

## 💻 Frontend Integration

### React Payment Component Example

```javascript
import { useState } from 'react';
import api from '../api/axios';

const PaymentForm = ({ booking }) => {
  const [cardDetails, setCardDetails] = useState({
    cardNumber: '',
    cardHolderName: '',
    expiryMonth: '',
    expiryYear: '',
    cvv: ''
  });
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handlePayment = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);

    try {
      const response = await api.post('/payments/process', {
        bookingId: booking.id,
        amount: booking.totalFare,
        paymentMethod: 'CREDIT_CARD',
        ...cardDetails
      });

      if (response.data.data.status === 'SUCCESS') {
        // Payment successful
        alert('Payment successful! Transaction ID: ' + 
              response.data.data.transactionId);
        // Redirect to booking confirmation
      } else {
        // Payment failed
        setError(response.data.data.statusMessage);
      }
    } catch (err) {
      setError('Payment processing error: ' + err.message);
    } finally {
      setLoading(false);
    }
  };

  return (
    <form onSubmit={handlePayment}>
      <h2>Payment Details</h2>
      <p>Amount: ₹{booking.totalFare}</p>
      
      {error && <div className="error">{error}</div>}
      
      <input
        type="text"
        placeholder="Card Number"
        value={cardDetails.cardNumber}
        onChange={(e) => setCardDetails({
          ...cardDetails, 
          cardNumber: e.target.value
        })}
        maxLength="16"
        required
      />
      
      <input
        type="text"
        placeholder="Card Holder Name"
        value={cardDetails.cardHolderName}
        onChange={(e) => setCardDetails({
          ...cardDetails, 
          cardHolderName: e.target.value
        })}
        required
      />
      
      {/* Add expiry and CVV fields */}
      
      <button type="submit" disabled={loading}>
        {loading ? 'Processing...' : `Pay ₹${booking.totalFare}`}
      </button>
    </form>
  );
};
```

---

## 📊 Payment State Machine

```
┌─────────┐
│  START  │
└────┬────┘
     │
     ▼
┌──────────────┐
│   PENDING    │◄────────┐
│  (Booking)   │         │
└──────┬───────┘         │
       │                 │
       │ Process Payment │
       │                 │
       ▼                 │
┌──────────────┐         │
│ PROCESSING   │         │
│ (Simulated)  │         │
└──────┬───────┘         │
       │                 │
       ├─────────────────┤
       │                 │
       ▼                 ▼
┌───────────┐    ┌──────────┐
│  SUCCESS  │    │  FAILED  │
└─────┬─────┘    └─────┬────┘
      │                │
      │                │ Retry?
      │                └────────┘
      │
      ▼
┌───────────┐
│CONFIRMED  │
│ (Booking) │
└─────┬─────┘
      │
      │ Cancel?
      ▼
┌───────────┐
│ REFUNDED  │
└───────────┘
```

---

## 🎓 Key Concepts

### Transaction Atomicity
- Payment and booking status updated in single transaction
- Rollback on failure ensures data consistency

### Idempotency
- Each payment attempt generates unique transaction ID
- Prevents duplicate charges

### Retry Logic
- Failed payments remain retryable
- Booking stays PENDING until successful payment

### Audit Trail
- All payment attempts logged with timestamps
- Gateway responses stored for debugging

---

## 🚀 Next Steps for Production

1. **Integrate Real Payment Gateway**
   - Choose provider (Razorpay, Stripe, etc.)
   - Obtain API credentials
   - Replace simulation logic

2. **Implement Webhooks**
   - Handle async payment notifications
   - Verify webhook signatures
   - Update payment status

3. **Add Payment Security**
   - PCI DSS compliance
   - Tokenization
   - 3D Secure authentication

4. **Enhance Error Handling**
   - Retry mechanisms
   - Exponential backoff
   - Dead letter queue

5. **Add Monitoring**
   - Payment success rate metrics
   - Failed payment analytics
   - Alert on anomalies

---

**Implementation Date:** December 21, 2025  
**Version:** 1.0 (Dummy/Development)  
**Status:** Development Only - NOT Production Ready
