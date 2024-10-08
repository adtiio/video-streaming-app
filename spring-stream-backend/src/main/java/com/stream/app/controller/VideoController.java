package com.stream.app.controller;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.stream.app.model.Video;
import com.stream.app.payload.CustomMessage;
import com.stream.app.services.VideoService;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

@RestController
@RequestMapping("/api/v1/videos/")
@CrossOrigin("*")
public class VideoController {

    public static final int CHUNK_SIZE=1024*1024; //1mb

    private VideoService videoService;
    public VideoController(VideoService videoService) {
        this.videoService = videoService;
    }
    

    @PostMapping()
    public ResponseEntity<?> create(
        @RequestParam("file")MultipartFile file,
        @RequestParam("title") String title,
        @RequestParam("description") String description,
        @RequestParam("imageUrl") String imageUrl
    ){
        Video video =new Video();
        video.setTitle(title);
        video.setDescription(description);
        video.setImageUrl(imageUrl);
        video.setVideoId(UUID.randomUUID().toString());

        Video savedVideo=videoService.save(video,file);
        if(savedVideo!=null){
            return ResponseEntity.status(HttpStatus.OK).body(savedVideo);
        }else{
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(CustomMessage.builder().message("Video not uploaded").success(false));
        }
    }

    @GetMapping("/stream/{videoId}")
    public ResponseEntity<Resource> stream(@PathVariable String videoId){

        Video video = videoService.get(videoId);
        String contentType= video.getContentType();
        String filePath= video.getFilePath();

        Resource resource=new FileSystemResource(filePath);

        if(contentType==null){
            contentType="application/octet-stream";
        }

        return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
    }

    @GetMapping()
    public List<Video> getAll(){
        return videoService.getAll();
    }

    //stream video in chunks
    @GetMapping("/stream/range/{videoId}")
    public ResponseEntity<Resource> streamVideoRange(
        @PathVariable String videoId,
        @RequestHeader(value="Range",required=false) String range
    ){
        System.out.println(range);
        Video video=videoService.get(videoId);
        Path path=Paths.get(video.getFilePath());

        Resource resource=new FileSystemResource(path);

        String contentType=video.getContentType();
        if(contentType==null){
            contentType="application/octet-stream";
        }
        long fileLength=path.toFile().length();

        if(range==null){
            return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType)).body(resource);
        }

        long rangeStart;
        long rangeEnd;

        String []ranges= range.replace("bytes=","").split("-");
        rangeStart=Long.parseLong(ranges[0]);

        rangeEnd=Math.min(rangeStart + CHUNK_SIZE-1,fileLength-1);
        // if(ranges.length>1) rangeEnd=Long.parseLong(ranges[1]);
        // else{
        //     rangeEnd=fileLength-1;
        // }
        // if(rangeEnd>fileLength-1) rangeEnd=fileLength-1;

        InputStream inputStream;
        try{
            inputStream=Files.newInputStream(path);
            inputStream.skip(rangeStart);
            long contentLength=rangeEnd-rangeStart+1;

            byte [] data=new byte[(int)contentLength];
            int read=inputStream.read(data,0,data.length);
            System.out.println("read(number of bytes) : - " +read+" start:-"+rangeStart+" end:-"+rangeEnd);
            
            
            HttpHeaders  header=new HttpHeaders();
            header.add("Content-Range","bytes "+rangeStart+"-"+rangeEnd+"/"+fileLength);
            header.add("Cache-Control","no-cache, no-store, must-revalidate");
            header.add("Praga", "no-cache");
            header.add("Expires", "0");
            header.add("X-Content-Type-Options", "nosniff");
            header.setContentLength(contentLength);

            return ResponseEntity.status(HttpStatus.PARTIAL_CONTENT)
                .headers(header)
                .contentType(MediaType.parseMediaType(contentType))
                .body(new ByteArrayResource(data));

        }catch(IOException ex){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
        

        


    }
   
}
