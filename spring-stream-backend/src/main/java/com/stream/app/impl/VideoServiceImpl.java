package com.stream.app.impl;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

import javax.imageio.ImageIO;

import java.io.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.nio.file.Files;

import com.stream.app.model.Video;
import com.stream.app.repository.VideoRepository;
import com.stream.app.services.VideoService;
import java.nio.file.StandardCopyOption;

import jakarta.annotation.PostConstruct;


@Service
public class VideoServiceImpl implements VideoService{

    @Value("${files.video}")
    String DIR;

    @Autowired
    private VideoRepository videoRepository;


    @PostConstruct
    public void init(){
        File file=new File(DIR);
        if(!file.exists()){
            file.mkdir();
            System.out.println("folder created");
        }else{
            System.out.println("Folder already exsists");
        }
    }

    @Override
    public Video save(Video video, MultipartFile file) {

        try{

        String fileName=file.getOriginalFilename();
        String contentType=file.getContentType();
        InputStream inputStream=file.getInputStream();
        
        
            //folder path : create
            String cleanFileName=StringUtils.cleanPath(fileName);
            String cleanFolder=StringUtils.cleanPath(DIR);

            Path path= Paths.get(cleanFolder,cleanFileName);

            Files.copy(inputStream,path,StandardCopyOption.REPLACE_EXISTING);

            //video mata data
            video.setContentType(contentType);
            video.setFilePath(path.toString());

            return videoRepository.save(video);

        }catch(IOException e){
            e.printStackTrace();
            return null;
        }

    }

    @Override
    public Video get(String videoId) {
        Video video=videoRepository.findById(videoId).orElseThrow(()->new RuntimeException("Video not found"));
        return video;
    }

    @Override
    public Video getByTitle(String title) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getByTitle'");
    }

    @Override
    public List<Video> getAll() {
        return videoRepository.findAll();
    }


}
