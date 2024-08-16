import { useEffect, useState } from 'react';
import { Toaster } from "react-hot-toast";
import './App.css';
import VideoList from './components/VideoList';
import VideoUpload from './components/VideoUpload';
import { BrowserRouter as Router, Route, Routes, Link, useLocation } from 'react-router-dom';

function App() {
  const [videos, setVideos] = useState([]);
  const [selectedVideo, setSelectedVideo] = useState(null);
  const location = useLocation();

  useEffect(() => {
    fetchVideos();
  }, []);

  const fetchVideos = async () => {
    try {
      const response = await fetch('http://localhost:8081/api/v1/videos/');
      const data = await response.json();
      setVideos(data);
    } catch (error) {
      console.error("Error fetching videos:", error);
    }
  };

  const handleVideoSelect = (video) => {
    setSelectedVideo(video);
    setVideos(videos.filter(v => v.videoId !== video.videoId).concat(selectedVideo ? [selectedVideo] : []));
  };

  return (
    <>
      <Toaster />
      <div className="container mx-auto px-4 py-6">
        <h1 className='flex justify-center text-3xl font-bold text-gray-700 dark:text-white mb-10'>
          Video Streaming App
        </h1>
        
        {/* Upload Video button */}
        {location.pathname !== '/upload' && (
          <div className="absolute top-4 right-4">
            <Link to="/upload">
              <button className="bg-blue-500 hover:bg-blue-700 text-white font-bold py-2 px-4 rounded">
                Upload Video
              </button>
            </Link>
          </div>
        )}

        {selectedVideo && (
          <div className="mb-8">
            <video
              className="w-full rounded-lg shadow-lg"
              style={{ maxHeight: '600px' }}
              src={`http://localhost:8081/api/v1/videos/stream/range/${selectedVideo.videoId}`}
              controls
              autoPlay
            />
            <h2 className="text-2xl font-semibold text-gray-900 dark:text-white mt-4">
              {selectedVideo.title}
            </h2>
            <p className="text-gray-700 dark:text-gray-300">
              {selectedVideo.description}
            </p>
          </div>
        )}

        <VideoList videos={videos} onVideoSelect={handleVideoSelect} />
      </div>
    </>
  );
}

export default function RootApp() {
  return (
    <Router>
      <Routes>
        <Route path="/" element={<App />} />
        <Route path="/upload" element={<VideoUpload />} />
      </Routes>
    </Router>
  );
}
