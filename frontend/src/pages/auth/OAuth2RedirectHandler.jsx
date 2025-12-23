import { useEffect, useState } from 'react';
import { useNavigate, useSearchParams } from 'react-router-dom';
import { useAuth } from '../../contexts/AuthContext';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';

const OAuth2RedirectHandler = () => {
  const navigate = useNavigate();
  const [searchParams] = useSearchParams();
  const { updateUser } = useAuth();
  const [error, setError] = useState('');

  useEffect(() => {
    const handleOAuth2Callback = async () => {
      try {
        // Get token from URL params
        const token = searchParams.get('token');
        const errorParam = searchParams.get('error');

        if (errorParam) {
          setError('OAuth2 authentication failed. Please try again.');
          setTimeout(() => navigate('/login'), 3000);
          return;
        }

        if (!token) {
          setError('No token received from OAuth2 provider.');
          setTimeout(() => navigate('/login'), 3000);
          return;
        }

        // Store token
        localStorage.setItem('accessToken', token);

        // Fetch user details
        const response = await fetch('http://localhost:8080/api/auth/me', {
          headers: {
            Authorization: `Bearer ${token}`,
          },
        });

        if (!response.ok) {
          throw new Error('Failed to fetch user details');
        }

        const apiResponse = await response.json();
        const userData = apiResponse.data; // Extract actual user

        localStorage.setItem('user', JSON.stringify(userData));
        updateUser(userData);


        // Redirect to home
        navigate('/', { replace: true });
      } catch (err) {
        console.error('OAuth2 callback error:', err);
        setError('Authentication failed. Redirecting to login...');
        setTimeout(() => navigate('/login'), 3000);
      }
    };

    handleOAuth2Callback();
  }, [searchParams, navigate, updateUser]);

  return (
    <div className="min-h-screen flex items-center justify-center bg-gray-50">
      <div className="text-center">
        {error ? (
          <Alert type="error" message={error} />
        ) : (
          <>
            <Spinner size="large" />
            <p className="mt-4 text-gray-600">Completing authentication...</p>
          </>
        )}
      </div>
    </div>
  );
};

export default OAuth2RedirectHandler;
