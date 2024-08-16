import React, { useState, useEffect } from 'react';
import axios from 'axios';
import { useNavigate } from 'react-router-dom';

const Home = () => {
  const [videos, setVideos] = useState([]);
  const navigate = useNavigate();

  useEffect(() => {
    axios.get('http://localhost:8081/api/v1/videos/')
      .then(response => setVideos(response.data))
      .catch(error => console.error('Error fetching videos:', error));
  }, []);

  const handleVideoClick = (videoId) => {
    navigate(`/?videoId=${videoId}`);
  };

  return (
    <div className="p-8 bg-gray-800 min-h-screen">
      <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4">
        {videos.map(video => (
          <div
            key={video.videoId}
            className="bg-gray-700 p-4 rounded cursor-pointer hover:bg-gray-600"
            onClick={() => handleVideoClick(video.videoId)}
          >
            <img src={video.thumbnailUrl} alt={video.title} className="w-full h-48 object-cover mb-2 rounded" />
            <h3 className="text-lg text-white">{video.title}</h3>
            <p className="text-gray-400">{video.description}</p>
          </div>
        ))}
      </div>
    </div>
  );
};

export default Home;
