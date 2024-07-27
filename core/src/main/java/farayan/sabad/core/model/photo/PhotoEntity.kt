package farayan.sabad.core.model.photo

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import farayan.sabad.core.model.product.ProductEntity
import java.util.UUID

@DatabaseTable(tableName = "photos")
class PhotoEntity {
    @DatabaseField(columnName = "id", generatedId = true)
    var id: Long = 0

    @DatabaseField(columnName = "globalId")
    var globalID: Long? = null

    @DatabaseField(columnName = "pendingSync")
    var pendingSync: Boolean = false

    @DatabaseField(columnName = "lastSynced")
    var lastSynced: Long? = null

    @DatabaseField(columnName = "alwaysId")
    var alwaysId: UUID = UUID.randomUUID()

    @DatabaseField(columnName = "path")
    var path: String = ""

    @DatabaseField(columnName = "product", foreign = true)
    var product: ProductEntity? = null

    @DatabaseField(columnName = "position")
    var position: Int = 0

    @DatabaseField(columnName = "widthPixels")
    var widthPixels: Int = 0

    @DatabaseField(columnName = "heightPixels")
    var heightPixels: Int = 0

    @DatabaseField(columnName = "widthHeightRatio")
    var widthHeightRatio: Double = 1.0

    @DatabaseField(columnName = "sizeInBytes")
    var sizeInBytes: Int = 0

    @DatabaseField(columnName = "type")
    var type: String = ""

}