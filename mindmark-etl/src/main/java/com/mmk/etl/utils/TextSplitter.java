package com.mmk.etl.utils;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.util.ArrayList;
import java.util.List;

/**
 * 基于HanLP的智能文本分割工具
 * 用于将长文本按照语义完整性分割成适合大模型处理的小片段
 * 
 * @author 大漠穷秋
 */
public class TextSplitter {
    /** 
     * 单个文本块的最大Token数量
     * 设置为模型最大容量(2048)的约90%作为安全余量
     * 预留余量的原因：
     * 1. HanLP分词结果与实际模型分词可能存在差异
     * 2. 特殊字符可能占用更多tokens
     * 3. 英文单词可能被模型切分为多个subwords
     */
    private static final int MAX_TOKENS = 1800;
    
    /** 单个文本块的最小Token数量，设置为最大值的约40% */
    private static final int MIN_TOKENS = 720;

    /**
     * 将输入文本智能分割成多个语义完整的片段
     * 
     * 分割策略：
     * 1. 使用HanLP进行智能分句，保证句子的完整性
     * 2. 对每个句子进行分词，精确计算token数量
     * 3. 当累积的token数量在合适范围内时，将多个句子组合成块
     * 4. 确保每个分割后的文本块既不会太大（超出模型限制）也不会太小（保持上下文）
     *
     * @param text 需要分割的原始文本
     * @return 分割后的文本片段列表，每个片段都是语义完整的
     * @throws RuntimeException 当HanLP处理文本失败时可能抛出异常
     */
    public static List<String> splitText(String text) {
        List<String> chunks = new ArrayList<>();
        List<String> sentences = HanLP.extractSummary(text, Integer.MAX_VALUE);
        
        StringBuilder currentChunk = new StringBuilder();
        int currentTokenCount = 0;
        for (String sentence : sentences) {
            List<Term> terms = HanLP.segment(sentence);
            int sentenceTokens = terms.size();
            if (currentTokenCount + sentenceTokens > MAX_TOKENS && currentTokenCount >= MIN_TOKENS) {
                chunks.add(currentChunk.toString());
                currentChunk = new StringBuilder();
                currentTokenCount = 0;
            }
            currentChunk.append(sentence);
            currentTokenCount += sentenceTokens;
        }
        if (currentChunk.length() > 0) {
            chunks.add(currentChunk.toString());
        }
        return chunks;
    }
}
