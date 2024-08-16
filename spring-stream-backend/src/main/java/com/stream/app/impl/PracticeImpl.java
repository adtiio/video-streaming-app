// package com.stream.app.impl;

// import java.io.File;
// import java.io.IOException;
// import java.io.InputStream;
// import java.nio.file.Files;
// import java.nio.file.Path;
// import java.nio.file.Paths;
// import java.nio.file.StandardCopyOption;
// import java.util.List;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;
// import org.springframework.util.StringUtils;
// import org.springframework.web.multipart.MultipartFile;

// import com.stream.app.model.Video;
// import com.stream.app.repository.VideoRepository;
// import com.stream.app.services.VideoService;

// import jakarta.annotation.PostConstruct;

// @Service
// public class PracticeImpl implements VideoService{

    
//     String DIR="temp/";

//     @Autowired
//     VideoRepository videoRepository;

//     @PostConstruct
//     public void init(){
//         File file=new File(DIR);
//         if(!file.exists()){
//             file.mkdirs();
//         }
//     }

//     @Override
//     public Video save(Video video, MultipartFile file) {
        
//         try{
//             String fileName=file.getOriginalFilename();
//             String contentType=file.getContentType();
//             InputStream inputStream=file.getInputStream();

            
//             fileName=StringUtils.cleanPath(fileName);
//             DIR=StringUtils.cleanPath(DIR);

//             Path path=Paths.get(DIR,fileName);

//             Files.copy(inputStream,path,StandardCopyOption.REPLACE_EXISTING);

//             video.setContentType(contentType);
//             video.setFilePath(path.toString());
//             return videoRepository.save(video);



//         }catch(IOException ex){
//             ex.printStackTrace();
//             return null;
//         }

//     }

//     @Override
//     public Video get(String videoId) {
//         return videoRepository.findById(videoId).orElseThrow(()->new RuntimeException("Video not found"));
//     }
//     @Override
//     public Video getByTitle(String title) {
//         // TODO Auto-generated method stub
//         throw new UnsupportedOperationException("Unimplemented method 'getByTitle'");
//     }

//     @Override
//     public List<Video> getAll() {
//         return videoRepository.findAll();
//     }

// }
