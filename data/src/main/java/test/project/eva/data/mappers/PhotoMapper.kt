package test.project.eva.data.mappers

import test.project.eva.data.models.PhotoEntity
import test.project.eva.domain.models.Photo

class PhotoMapper : EntityMapper<PhotoEntity, Photo> {

    override fun mapFromEntity(entity: PhotoEntity): Photo {
        return Photo(
            imageUrl = entity.urls?.regular ?: ""
        )
    }

    // Just a plug
    override fun mapToEntity(domainModel: Photo): PhotoEntity {
        return PhotoEntity(
            urls = null
        )
    }
}