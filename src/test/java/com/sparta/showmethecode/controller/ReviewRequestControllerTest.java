package com.sparta.showmethecode.controller;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(MockitoExtension.class)
@WebMvcTest
public class ReviewRequestControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ReviewRequestController reviewRequestController;



}
