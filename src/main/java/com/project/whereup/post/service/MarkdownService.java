package com.project.whereup.post.service;

import lombok.RequiredArgsConstructor;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.springframework.stereotype.Service;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class MarkdownService {
    private final Parser parser;
    private final HtmlRenderer renderer;

    public MarkdownService() {
        this.parser = Parser.builder().build();
        this.renderer = HtmlRenderer.builder().build();
    }
    // 마크다운으로 작성된 글을 화면에 해석해서 띄워줄 거, 엔터 안먹는거 고침
    public String renderMarkdownToHtml(String markdown) {
        markdown = markdown.replaceAll("\n", "<br>");
        Node document = parser.parse(markdown);
        return renderer.render(document);
    }

    // content 뒤져서 이미지 있나 찾는거, 있으면 첫번째꺼 반환
    public String isImageInContent(String content) {
        String regex = "!\\[[^\\[\\]]*\\]\\(([^\\(\\)]+)\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(content);

        if (matcher.find()) {
            return matcher.group(1);
        }
        return null;
    }
}
