package com.example.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.domain.Bookmark;
import com.example.repository.BookmarkRepository;

@Service
@Transactional
public class BookmarkService {
	@Autowired
	BookmarkRepository bookmarkRepository;
	
	public List<Bookmark> findAll() {
		return bookmarkRepository.findAll(new Sort(Sort.Direction.ASC, "id"));
	}
	
	public Bookmark getBookmark(Long id) {
		return bookmarkRepository.findOne(id);
	}
	
	public Bookmark save(Bookmark bookmark) {
		return bookmarkRepository.save(bookmark);
	}
	
	public void delete(Long id) {
		bookmarkRepository.delete(id);
	}
}
