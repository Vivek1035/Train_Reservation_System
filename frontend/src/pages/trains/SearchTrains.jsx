import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { trainService } from '../../services/trainService';
import Input from '../../components/common/Input';
import Button from '../../components/common/Button';
import Card from '../../components/common/Card';
import Spinner from '../../components/common/Spinner';
import Alert from '../../components/common/Alert';
import { Search, Calendar, MapPin, Clock, ArrowRight } from 'lucide-react';
import { format } from 'date-fns';

const SearchTrains = () => {
    const navigate = useNavigate();
    const [stations, setStations] = useState([]);
    const [searchParams, setSearchParams] = useState({
        source: '',
        destination: '',
        journeyDate: format(new Date(), 'yyyy-MM-dd'),
    });
    const [trains, setTrains] = useState([]);
    const [loading, setLoading] = useState(false);
    const [searching, setSearching] = useState(false);
    const [error, setError] = useState('');

    useEffect(() => {
        fetchStations();
    }, []);

    const fetchStations = async () => {
        try {
            setLoading(true);
            const data = await trainService.getAllStations();
            setStations(data);
        } catch (err) {
            console.error('Error fetching stations:', err);
            setError('Failed to load stations');
        } finally {
            setLoading(false);
        }
    };

    const handleSearch = async (e) => {
        e.preventDefault();
        setError('');

        if (!searchParams.source || !searchParams.destination) {
            setError('Please select both source and destination');
            return;
        }

        if (searchParams.source === searchParams.destination) {
            setError('Source and destination cannot be the same');
            return;
        }

        try {
            setSearching(true);
            const data = await trainService.searchTrains({
                sourceStationId: searchParams.source,
                destinationStationId: searchParams.destination,
                journeyDate: searchParams.journeyDate,
            });
            setTrains(data);
        } catch (err) {
            console.error('Search error:', err);
            setError('Failed to search trains. Please try again.');
        } finally {
            setSearching(false);
        }
    };

    const handleTrainSelect = (train) => {
        navigate(`/trains/${train.id}/seats`, {
            state: {
                train,
                searchParams
            }
        });
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
            <div className="max-w-6xl mx-auto px-4">
                {/* Search Form */}

                <Card className="mb-8">
                    <h1 className="text-2xl font-bold text-gray-900 mb-6 flex items-center gap-2">
                        <Search size={28} />
                        Search Trains
                    </h1>

                    {error && (
                        <Alert type="error" message={error} onClose={() => setError('')} className="mb-4" />
                    )}

                    <form onSubmit={handleSearch} className="space-y-4">
                        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    <MapPin size={16} className="inline mr-1" />
                                    From
                                </label>
                                <select
                                    className="input"
                                    value={searchParams.source}
                                    onChange={(e) =>
                                        setSearchParams((prev) => ({ ...prev, source: e.target.value }))
                                    }
                                    required
                                >
                                    <option value="">Select Source Station</option>
                                    {stations.map((station) => (
                                        <option key={station.id} value={station.id}>
                                            {station.stationName} ({station.stationCode})
                                        </option>
                                    ))}

                                </select>
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    <MapPin size={16} className="inline mr-1" />
                                    To
                                </label>
                                <select
                                    className="input"
                                    value={searchParams.destination}
                                    onChange={(e) =>
                                        setSearchParams((prev) => ({ ...prev, destination: e.target.value }))
                                    }
                                    required
                                >
                                    <option value="">Select Destination Station</option>
                                    {stations.map((station) => (
                                        <option key={station.id} value={station.id}>
                                            {station.stationName} ({station.stationCode})
                                        </option>
                                    ))}
                                </select>
                            </div>

                            <div>
                                <label className="block text-sm font-medium text-gray-700 mb-1">
                                    <Calendar size={16} className="inline mr-1" />
                                    Journey Date
                                </label>
                                <input
                                    type="date"
                                    className="input"
                                    value={searchParams.journeyDate}
                                    min={format(new Date(), 'yyyy-MM-dd')}
                                    onChange={(e) =>
                                        setSearchParams((prev) => ({ ...prev, journeyDate: e.target.value }))
                                    }
                                    required
                                />
                            </div>
                        </div>

                        <Button type="submit" variant="primary" className="w-full md:w-auto" loading={searching}>
                            <Search size={18} />
                            Search Trains
                        </Button>
                    </form>
                </Card>

                {/* Search Results */}
                {searching ? (
                    <div className="flex justify-center py-12">
                        <Spinner size="large" />
                    </div>
                ) : trains.length > 0 ? (
                    <div className="space-y-4">
                        <h2 className="text-xl font-semibold text-gray-900">
                            {trains.length} Train{trains.length !== 1 ? 's' : ''} Found
                        </h2>
                        {trains.map((train) => (
                            <Card key={train.id} hover className="cursor-pointer" onClick={() => handleTrainSelect(train)}>
                                <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                                    <div className="flex-1">
                                        <h3 className="text-lg font-semibold text-gray-900">
                                            {train.trainNumber} - {train.trainName}
                                        </h3>
                                        <div className="flex items-center gap-4 mt-2 text-sm text-gray-600">
                                            <div className="flex items-center gap-1">
                                                <Clock size={16} />
                                                <span>{train.departureTime}</span>
                                            </div>
                                            <ArrowRight size={16} />
                                            <div className="flex items-center gap-1">
                                                <Clock size={16} />
                                                <span>{train.arrivalTime}</span>
                                            </div>
                                        </div>
                                        {train.totalSeats && (
                                            <p className="text-sm text-gray-500 mt-1">
                                                Available Seats: {train.availableSeats || 'Check availability'}
                                            </p>
                                        )}
                                    </div>
                                    <Button variant="primary">View Seats</Button>
                                </div>
                            </Card>
                        ))}
                    </div>
                ) : (
                    <Card>
                        <div className="text-center py-12">
                            <Search size={48} className="mx-auto text-gray-400 mb-4" />
                            <p className="text-gray-600">
                                {trains.length === 0 && !searching
                                    ? 'Search for trains to see available options'
                                    : 'No trains found for the selected route and date'}
                            </p>
                        </div>
                    </Card>
                )}
            </div>
        </div>
    );
};

export default SearchTrains;
