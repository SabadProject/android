package farayan.sabad.core.FaraBank;

import java.util.List;

import farayan.sabad.core.OnePlace.Invoice.InvoicePortable;
import farayan.sabad.core.OnePlace.Store.StorePortable;
import farayan.sabad.core.OnePlace.Unit.UnitPortable;
import farayan.sabad.core.model.product.ProductPortable;

public class FaraBankInvoice {
    public final InvoicePortable Invoice;
    public final StorePortable Store;
    public final List<ProductPortable> Products;
    public final List<UnitPortable> Units;

    public FaraBankInvoice(
            InvoicePortable invoice,
            StorePortable store,
            List<ProductPortable> products,
            List<UnitPortable> units
    ) {
        Invoice = invoice;
        Store = store;
        Products = products;
        Units = units;
    }
}
