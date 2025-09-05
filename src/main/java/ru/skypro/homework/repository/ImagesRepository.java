package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.skypro.homework.entity.Image;

public interface ImagesRepository extends JpaRepository<Image, Integer> {
    Image findByPath(String path);
}
