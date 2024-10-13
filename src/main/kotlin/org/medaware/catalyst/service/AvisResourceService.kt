package org.medaware.catalyst.service

import jakarta.annotation.PostConstruct
import org.medaware.avis.resource.AvisCallback
import org.medaware.catalyst.config.catalyst.CatalystServerConfiguration
import org.springframework.stereotype.Service

@Service
class AvisResourceService(
    val minioService: MinIoService,
    val catalystServerConfiguration: CatalystServerConfiguration
) {

    @PostConstruct
    fun attachCallback() {
        AvisCallback.resourceCallback = { url ->
            handleResourceRequest(url)
        }
    }

    fun handleResourceRequest(url: String): String =
        catalystServerConfiguration.server + "/rsrc/" + minioService.storeOrRetrieveResource(url)

}