import React, { useState } from 'react';
import videoLogo from "../assets/upload.png";
import { Alert, Button, Card, Label, Progress, TextInput, Textarea } from "flowbite-react";
import axios from 'axios';
import toast from 'react-hot-toast';
import { Link } from 'react-router-dom';

const VideoUpload = () => {
  const [selectedFile, setSelectedFile] = useState(null);
  const [progress, setProgress] = useState(0);
  const [uploading, setUploading] = useState(false);
  const [message, setMessage] = useState("");
  const [meta, setMeta] = useState({
    title: "",
    description: "",
    imageUrl: "",
  });

  function handleFileChange(event) {
    setSelectedFile(event.target.files[0]);
  }

  function formFieldChange(event) {
    setMeta({
      ...meta,
      [event.target.name]: event.target.value,
    });
  }

  function handleForm(formEvent) {
    formEvent.preventDefault();
    if (!selectedFile) {
      alert("Select File !!");
      return;
    }

    saveVideoToServer(selectedFile, meta);
  }

  function resetForm() {
    setMeta({
      title: "",
      description: "",
      imageUrl: "",
    });
    setSelectedFile(null);
    setUploading(false);
  }

  async function saveVideoToServer(video, videoMetaData) {
    setUploading(true);
    try {
      let formData = new FormData();
      formData.append("title", videoMetaData.title);
      formData.append("description", videoMetaData.description);
      formData.append("imageUrl", videoMetaData.imageUrl);
      formData.append("file", selectedFile);

      let response = await axios.post(`http://localhost:8081/api/v1/videos/`, formData, {
        headers: {
          'Content-Type': 'multipart/form-data',
        },
        onUploadProgress: (progressEvent) => {
          const progress = Math.round((progressEvent.loaded * 100) / progressEvent.total);
          setProgress(progress);
        },
      });
      console.log(response);
      setProgress(0);
      setMessage("File Uploaded");
      setUploading(false);
      toast.success("File Uploaded Successfully !!");
      resetForm();
    } catch (error) {
      console.log(error);
      setMessage("Error in uploading File");
      setUploading(false);
      toast.error("File not Uploaded !!");
    }
  }

  return (
    <div className=' text-white flex justify-center '>
      <Card className='flex flex-col items-center justify-center mt-100' style={{ maxWidth: '400px' }}>
        <h1>Upload Videos</h1>
        <form noValidate className='flex flex-col space-y-5' onSubmit={handleForm}>

          <div>
            <div className="mb-2 block">
              <Label htmlFor="file-upload" value="Video Title" />
            </div>
            <TextInput value={meta.title} onChange={formFieldChange} name='title' placeholder='Enter title' />
          </div>

          <div className="max-w-md">
            <div className="mb-2 block">
              <Label htmlFor="comment" value="Video Description" />
            </div>
            <Textarea value={meta.description} onChange={formFieldChange} name='description' id="comment" placeholder="Write your video description..." required rows={4} />
          </div>

          <div className="max-w-md">
            <div className="mb-2 block">
              <Label htmlFor="comment" value="Video thumbnail" />
            </div>
            <TextInput value={meta.imageUrl} onChange={formFieldChange} name='imageUrl' id="comment" placeholder="Paste Image Url..." required rows={4} />
          </div>

          <div className='flex items-center space-x-5 justify-center'>
            <div className="shrink-0">
              <img className="h-16 w-16 object-cover" src={videoLogo} alt="Current profile photo" />
            </div>
            <label className="block">
              <span className="sr-only">Choose video file</span>
              <input name='file' onChange={handleFileChange} type="file" className="block w-full text-sm text-slate-500
                file:mr-4 file:py-2 file:px-4
                file:rounded-full file:border-0
                file:text-sm file:font-semibold
                file:bg-violet-50 file:text-violet-700
                hover:file:bg-violet-100
              "/>
            </label>
          </div>

          <div className=''>
            {uploading && (
              <Progress progress={progress} textLabel="Uploading.." size="lg" labelProgress labelText />
            )}
          </div>

          <div className='flex justify-center space-x-4'>
            <Button disabled={uploading} type="submit">Submit</Button>
            <Link to="/">
              <Button color="gray">Back to Home</Button>
            </Link>
          </div>

        </form>
      </Card>
    </div>
  );
}

export default VideoUpload;
