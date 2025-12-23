import { useState, useEffect } from 'react';
import { trainService } from '../../services/trainService';
import Card from '../../components/common/Card';
import Button from '../../components/common/Button';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';
import Modal from '../../components/common/Modal';
import { Train, Plus, Edit, Trash2 } from 'lucide-react';

const ManageTrains = () => {
  const [trains, setTrains] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState('');
  const [deleteModal, setDeleteModal] = useState({ open: false, train: null });
  const [deleting, setDeleting] = useState(false);

  useEffect(() => {
    fetchTrains();
  }, []);

  const fetchTrains = async () => {
    try {
      setLoading(true);
      const data = await trainService.getAllTrains();
      setTrains(Array.isArray(data) ? data : data.content || []);
    } catch (err) {
      console.error('Error fetching trains:', err);
      setError('Failed to load trains');
    } finally {
      setLoading(false);
    }
  };

  const handleDelete = async () => {
    if (!deleteModal.train) return;

    try {
      setDeleting(true);
      await trainService.deleteTrain(deleteModal.train.id);
      setTrains(trains.filter((t) => t.id !== deleteModal.train.id));
      setDeleteModal({ open: false, train: null });
    } catch (err) {
      console.error('Delete error:', err);
      setError('Failed to delete train. It may have existing bookings.');
    } finally {
      setDeleting(false);
    }
  };

  if (loading) {
    return (
      <div className="flex items-center justify-center min-h-screen">
        <Spinner size="large" />
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 py-8">
      <div className="max-w-7xl mx-auto px-4">
        <div className="flex items-center justify-between mb-6">
          <h1 className="text-2xl font-bold text-gray-900 flex items-center gap-2">
            <Train size={28} />
            Manage Trains
          </h1>
          <Button variant="primary">
            <Plus size={18} />
            Add New Train
          </Button>
        </div>

        {error && (
          <Alert type="error" message={error} onClose={() => setError('')} className="mb-4" />
        )}

        {trains.length === 0 ? (
          <Card>
            <div className="text-center py-12">
              <Train size={48} className="mx-auto text-gray-400 mb-4" />
              <p className="text-gray-600">No trains available</p>
            </div>
          </Card>
        ) : (
          <div className="grid grid-cols-1 lg:grid-cols-2 gap-4">
            {trains.map((train) => (
              <Card key={train.id}>
                <div className="flex items-start justify-between">
                  <div className="flex-1">
                    <h3 className="text-lg font-semibold text-gray-900">
                      {train.trainNumber} - {train.trainName}
                    </h3>
                    <div className="mt-2 space-y-1 text-sm text-gray-600">
                      <p>Source: {train.sourceStation?.name}</p>
                      <p>Destination: {train.destinationStation?.name}</p>
                      <p>Departure: {train.departureTime}</p>
                      <p>Arrival: {train.arrivalTime}</p>
                      {train.totalSeats && <p>Total Seats: {train.totalSeats}</p>}
                    </div>
                  </div>
                  <div className="flex flex-col gap-2">
                    <Button variant="secondary" size="sm">
                      <Edit size={16} />
                    </Button>
                    <Button
                      variant="danger"
                      size="sm"
                      onClick={() => setDeleteModal({ open: true, train })}
                    >
                      <Trash2 size={16} />
                    </Button>
                  </div>
                </div>
              </Card>
            ))}
          </div>
        )}

        {/* Delete Confirmation Modal */}
        <Modal
          isOpen={deleteModal.open}
          onClose={() => setDeleteModal({ open: false, train: null })}
          title="Delete Train"
          size="sm"
        >
          <div className="space-y-4">
            <p className="text-gray-600">
              Are you sure you want to delete train{' '}
              <strong>
                {deleteModal.train?.trainNumber} - {deleteModal.train?.trainName}
              </strong>
              ?
            </p>
            <Alert
              type="warning"
              message="This action cannot be undone. The train will be permanently removed."
            />
            <div className="flex gap-3">
              <Button
                variant="danger"
                className="flex-1"
                onClick={handleDelete}
                loading={deleting}
                disabled={deleting}
              >
                Yes, Delete
              </Button>
              <Button
                variant="secondary"
                className="flex-1"
                onClick={() => setDeleteModal({ open: false, train: null })}
                disabled={deleting}
              >
                Cancel
              </Button>
            </div>
          </div>
        </Modal>
      </div>
    </div>
  );
};

export default ManageTrains;
