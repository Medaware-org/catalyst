package org.medaware.catalyst.service

import jakarta.annotation.PostConstruct
import org.medaware.anterogradia.Anterogradia
import org.medaware.anterogradia.runtime.Runtime
import org.medaware.avis.MedawareDesignKit
import org.springframework.stereotype.Service

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