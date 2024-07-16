package farayan.sabad.core.model.unit

import com.j256.ormlite.field.DatabaseField
import com.j256.ormlite.table.DatabaseTable
import farayan.commons.FarayanUtility
import farayan.commons.UI.Core.IBoxEntity
import farayan.sabad.core.OnePlace.Unit.UnitSchema
import farayan.sabad.core.base.SabadEntityBase
import farayan.sabad.core.commons.UnitVariations

@DatabaseTable(tableName = UnitSchema.Units)
class UnitEntity : SabadEntityBase<UnitEntity>, IBoxEntity {
    @DatabaseField(columnName = UnitSchema.DisplayableName)
    var displayableName: String? = null

    @DatabaseField(columnName = UnitSchema.QueryableName)
    var queryableName: String? = null

    @DatabaseField(columnName = UnitSchema.Category)
    var category: String? = null

    @DatabaseField(columnName = UnitSchema.Variation)
    var variation: UnitVariations? = null

    constructor()

    constructor(name: String?, category: String?, variation: UnitVariations?) {
        displayableName = FarayanUtility.Displayable(name)
        queryableName = FarayanUtility.Queryable(name)
        this.category = category
        this.variation = variation
    }

    override fun NeedsRefresh(): Boolean {
        return displayableName == null
    }

    override fun getTitle(): String {
        return displayableName!!
    }

    override fun setTitle(title: String) {
        queryableName = FarayanUtility.Queryable(title)
        displayableName = FarayanUtility.Displayable(title)
    }
}