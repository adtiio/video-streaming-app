// VideoList.jsx
import React from 'react';

const VideoList = ({ videos, onVideoSelect }) => {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-6">
      {videos.map((video) => (
        <div
          key={video.videoId}
          className="cursor-pointer group relative"
          onClick={() => onVideoSelect(video)}
        >
          <img
            src={video.imageUrl} // Placeholder thumbnail image
            alt={video.title}
            className="w-full h-60 object-cover rounded-lg group-hover:opacity-75"
          />
          <div className="mt-2">
            <h3 className="text-sm font-semibold text-gray-900 dark:text-white">{video.title}</h3>
            <p className="text-xs text-gray-500 dark:text-gray-400">{video.description}</p>
          </div>
        </div>
      ))}
    </div>
  );
};

export default VideoList;
