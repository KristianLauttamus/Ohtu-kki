package com.ohtukki.citations.test.controllers;

import com.ohtukki.citations.Application;
import com.ohtukki.citations.data.DatabaseJsonDao;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
@WebAppConfiguration
public class CitationControllerTest {
    @Autowired
    private WebApplicationContext webAppContext;
    
    private MockMvc mockMvc;
    private DatabaseJsonDao database;

    @Before
    public void setUp() throws Exception {
        // Needed for Thymeleaf
        webAppContext.getServletContext().setAttribute(
                WebApplicationContext.ROOT_WEB_APPLICATION_CONTEXT_ATTRIBUTE, webAppContext);
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webAppContext).build();
        
        this.database = new DatabaseJsonDao(DatabaseJsonDao.DEFAULT_FILE);
        this.database.loadJson();
    }
    
    @Test
    public void postArticleCitation() throws Exception {
        int size = this.database.all().size();
        
        mockMvc.perform(post("/citation")
                .param("type", "article")
                .param("author", "Author")
                .param("name", "Name")
                .param("journal", "Journal")
                .param("year", "Year"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        
        this.database.loadJson();
        assertEquals("Author", this.database.all().get(this.database.all().size()-1).getAuthor());
    }
    
    @Test
    public void postArticleCitationWithOptionals() throws Exception {
        int size = this.database.all().size();
        
        mockMvc.perform(post("/citation")
                .param("type", "article")
                .param("article-author", "Author")
                .param("article-name", "Name")
                .param("article-journal", "Journal")
                .param("article-year", "Year")
                
                .param("article-volume", "Volume")
                .param("article-number", "Number")
                .param("article-pages", "Pages")
                .param("article-month", "Month")
                .param("article-note", "Note")
                .param("article-key", "Key"))
                .andExpect(status().is3xxRedirection())
                .andReturn();
        
        this.database.loadJson();
        assertEquals(size+1, this.database.all().size());
    }
        
}