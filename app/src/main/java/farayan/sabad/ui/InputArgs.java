package farayan.sabad.ui;

import com.google.zxing.client.android.BeepManager;

import farayan.commons.UI.Core.IGenericEvent;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo;
import farayan.sabad.core.OnePlace.InvoiceItem.IInvoiceItemRepo;
import farayan.sabad.core.OnePlace.InvoiceItem.InvoiceItemEntity;
import farayan.sabad.core.OnePlace.Product.IProductRepo;
import farayan.sabad.core.OnePlace.Product.ProductEntity;
import farayan.sabad.core.OnePlace.ProductBarcode.CapturedBarcode;
import farayan.sabad.core.OnePlace.ProductBarcode.IProductBarcodeRepo;
import farayan.sabad.core.OnePlace.Unit.IUnitRepo;

public class InputArgs {
    final GroupEntity Group;
    private final InvoiceItemEntity InvoiceItem;
    private final IGroupRepo GroupRepo;
    private final IGroupUnitRepo GroupUnitRepo;
    private final IProductRepo ProductRepo;
    private final IProductBarcodeRepo ProductBarcodeRepo;
    private final IInvoiceItemRepo InvoiceItemRepo;
    private final IUnitRepo UnitRepo;
    private final com.google.zxing.client.android.BeepManager BeepManager;
    private final ProductEntity Product;
    private final IGenericEvent<InvoiceItemEntity> OnRegistered;
    private final IGenericEvent<InvoiceItemEntity> OnRemoved;
    private CapturedBarcode Barcode;

    public InputArgs(
            InvoiceItemEntity invoiceItem,
            GroupEntity group,
            ProductEntity product,
            CapturedBarcode barcode,
            IGenericEvent<InvoiceItemEntity> onRegistered,
            IGenericEvent<InvoiceItemEntity> onRemoved,
            IGroupRepo groupRepo,
            IGroupUnitRepo groupUnitRepo,
            IProductRepo productRepo,
            IProductBarcodeRepo productBarcodeRepo,
            IInvoiceItemRepo invoiceItemRepo,
            IUnitRepo unitRepo,
            BeepManager beepManager
    ) {
        InvoiceItem = invoiceItem;
        GroupRepo = groupRepo;
        GroupUnitRepo = groupUnitRepo;
        ProductRepo = productRepo;
        ProductBarcodeRepo = productBarcodeRepo;
        InvoiceItemRepo = invoiceItemRepo;
        UnitRepo = unitRepo;
        BeepManager = beepManager;
        Group = group;
        Product = product;
        Barcode = barcode;
        OnRegistered = onRegistered;
        OnRemoved = onRemoved;
    }
}
