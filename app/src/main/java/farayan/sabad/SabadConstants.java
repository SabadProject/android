package farayan.sabad;

import com.google.zxing.BarcodeFormat;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

import farayan.commons.Commons.Rial;

public interface SabadConstants
{
	Collection<BarcodeFormat> SupportedBarcodeFormats = Collections.singletonList(BarcodeFormat.EAN_13);
	int DataChangedResultCode = 100;

	int NewGroupGuideDisplayMaxCount = 5;
	long NewGroupGuideDisplayMinInterval = 60 * 1000;

	int NeedChangeGuideDisplayMaxCount = 5;
	long NeedChangeGuideDisplayMinInterval = 60 * 1000;

	int PickChangeGuideDisplayMaxCount = 5;
	long PickChangeGuideDisplayMinInterval = 60 * 1000;

	int GroupNewClickedGuideDisplayMaxCount = 5;
	long GroupNewClickedGuideDisplayMinInterval = 60 * 1000;

	int InvoiceItemRegisteredGuideDisplayMaxCount = 5;
	long InvoiceItemRegisteredGuideDisplayMinInterval = 60 * 1000;

	int CheckoutGuideDisplayMaxCount = 5;
	long CheckoutGuideDisplayMinInterval = 60 * 1000;

	int CheckoutDialogDisplayMaxCount = Integer.MAX_VALUE;
	long CheckoutDialogDisplayMinInterval = 5 * 60 * 1000;

	Rial.Coefficients TheCoefficient = Rial.Coefficients.Rial;
	int FaraBankExportInvoiceRequestCode = 1;
}
