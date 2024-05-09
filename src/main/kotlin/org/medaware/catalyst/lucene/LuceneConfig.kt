package org.medaware.catalyst.lucene

import org.apache.lucene.analysis.standard.StandardAnalyzer
import org.apache.lucene.index.IndexWriterConfig
import org.apache.lucene.index.IndexWriterConfig.OpenMode
import org.apache.lucene.store.ByteBuffersDirectory
import org.apache.lucene.store.Directory
import org.springframework.context.ApplicationContext
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class LuceneConfig(
    val context: ApplicationContext
) {

    @Bean
    fun luceneDirectory(): Directory {
        return ByteBuffersDirectory()
    }

    @Bean
    fun luceneAnalyzer(): StandardAnalyzer {
        return StandardAnalyzer()
    }

    @Bean
    fun luceneIndexWriterConfig(): IndexWriterConfig {
        val cfg = IndexWriterConfig(context.getBean(StandardAnalyzer::class.java))
        cfg.openMode = OpenMode.CREATE_OR_APPEND
        return cfg
    }

}