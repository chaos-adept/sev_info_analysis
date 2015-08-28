package com.chaoslabgames.bigbro.analyzer.tokenizer;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.Tokenizer;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishAnalyzer;
import org.apache.lucene.analysis.miscellaneous.ASCIIFoldingFilter;
import org.apache.lucene.analysis.miscellaneous.LengthFilter;
import org.apache.lucene.analysis.miscellaneous.TruncateTokenFilter;
import org.apache.lucene.analysis.ngram.NGramTokenFilter;
import org.apache.lucene.analysis.ru.RussianAnalyzer;
import org.apache.lucene.analysis.ru.RussianLightStemFilter;
import org.apache.lucene.analysis.util.CharArraySet;

public class LowercaseWhitespaceTokenizer extends Analyzer {

    private int minTokenLen;
    private int maxTokenLen;

    public LowercaseWhitespaceTokenizer(int minTokenLen, int maxTokenLen) {
        this.minTokenLen = minTokenLen;
        this.maxTokenLen = maxTokenLen;
    }

    @Override
    protected TokenStreamComponents createComponents(final String fieldName) {
        Tokenizer source = new org.apache.lucene.analysis.core.LowerCaseTokenizer();
        CharArraySet stopWords = RussianAnalyzer.getDefaultStopSet();
        stopWords.addAll(EnglishAnalyzer.getDefaultStopSet());

        StopFilter stopWordFilter = new StopFilter(source, stopWords);
        TokenStream asciiFoldingFilter = new ASCIIFoldingFilter(stopWordFilter);
        TokenStream lenFilter = new LengthFilter(asciiFoldingFilter, minTokenLen, maxTokenLen);
        TokenStream stemming = new RussianLightStemFilter(lenFilter);
        TokenStream filter = stemming;

        return new TokenStreamComponents(source, filter);
    }
}
