package com.aurora.community.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.catalina.connector.InputBuffer;
import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.tomcat.util.buf.Ascii;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SensitiveFilter {

    private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);

    private static final String REPLACEMENT = "***";

    private TrieNode rootNode = new TrieNode();

    // call after server start
    @PostConstruct
    public void init() {
        try (
            InputStream is = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        ){
            String keyword;
            while((keyword = reader.readLine()) != null) {
                // add to the trie
                this.addKeyword(keyword);
            }
            
        } catch (IOException e) {
            logger.error("sensitive-words file load fail", e.getMessage());
        }
    }

    // add a keyword to trie
    private void addKeyword(String keyword) {
        TrieNode tmpNode = rootNode;
        for (int i = 0; i < keyword.length(); i++) {
            char c = keyword.charAt(i);
            TrieNode subNode = tmpNode.getSubNode(c);
            if (subNode == null) {
                subNode = new TrieNode();
                tmpNode.addSubNode(c, subNode);
            }
            tmpNode = subNode;
            // lead
            if (i == keyword.length() - 1) {
                tmpNode.setKeywordEnd(true);
            }
        }
    }
    
    /**
     * filter the sensitive word
     * @param text needed to be filtered text
     * @return text after being filtered
     */
    public String filter(String text) {
        if (StringUtils.isBlank(text)) {
            return null;
        }
        TrieNode tmpNode = rootNode;
        // two pointers
        int begin = 0;
        int position = 0;
        // result
        StringBuilder sb = new StringBuilder();
        while (position < text.length()) {
            char ch = text.charAt(position);
            if (isSymble(ch)) {
                if (tmpNode == rootNode) {
                    sb.append(ch);
                    begin++;
                }
                position++;
                continue;
            }
            tmpNode = tmpNode.getSubNode(ch);
            if (tmpNode == null) {
                sb.append(text.charAt(begin));
                begin++;
                position = begin;
                tmpNode = rootNode;
            } else if (tmpNode.isKeywordEnd()) {
                // find the sensitive word [begin, position]
                sb.append(REPLACEMENT);
                position++;
                begin = position;
                tmpNode = rootNode;
            } else {
                position++;
            }
        }
        // there can be begin != .length - 1
        sb.append(text.substring(begin));
        return sb.toString();

    }

    private boolean isSymble(Character ch) {
        // 0x2E80-0x9EEE is asian letter
        return !CharUtils.isAsciiAlphanumeric(ch) && (ch < 0x2E80 || ch > 0x9FFF);
    }

    // prefix tree - Trie
    private class TrieNode {
        // key word endding
        private boolean isKeywordEnd = false;
        // current node's children, key is sub node's character
        private Map<Character, TrieNode> subNodes = new HashMap<>();

        public boolean isKeywordEnd() {
            return isKeywordEnd;
        }

        public void setKeywordEnd(boolean keywordEnd) {
            isKeywordEnd = keywordEnd;
        }

        public void addSubNode(Character c, TrieNode node) {
            subNodes.put(c, node);
        }

        public TrieNode getSubNode(Character c) {
            return subNodes.get(c);
        }
    }
}