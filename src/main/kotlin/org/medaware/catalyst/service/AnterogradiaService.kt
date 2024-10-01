package org.medaware.catalyst.service

import jakarta.annotation.PostConstruct
import org.medaware.anterogradia.Anterogradia
import org.medaware.anterogradia.runtime.Runtime
import org.medaware.avis.MedawareDesignKit
import org.medaware.catalyst.persistence.model.MaintainerEntity
import org.medaware.catalyst.persistence.repository.MaintainerRepository
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class AnterogradiaService {

    @PostConstruct
    fun configAnterogradia() {
        Anterogradia.enableLogging = false
    }

    fun runtimeWithAvis(): Runtime {
        return Runtime().let { runtime ->
            runtime.loadLibrary(MedawareDesignKit::class.java.canonicalName, "avis")
            return@let runtime
        }
    }

}