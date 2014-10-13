package com.example.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.domain.Bookmark;

public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

}
