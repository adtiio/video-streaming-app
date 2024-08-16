package com.stream.app.model;

import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.Data;

@Entity
@Data
public class Course {

    @Id
    private String id;

    private String title;

    @OneToMany(mappedBy = "course")
    private List<Video> list=new ArrayList<>();
}
