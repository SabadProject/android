package farayan.sabad.ui;

import android.content.Intent;
import android.net.Uri;
import android.view.View;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.FarayanUtility;
import farayan.sabad.contracts.IVendorContract;
import farayan.sabad.contracts.VendorUpdateStates;
import farayan.sabad.contracts.VendorUpdatesPriorities;
import farayan.sabad.R;

import static farayan.sabad.contracts.IVendorContractKt.UpdateActivityResultCode;

@AndroidEntryPoint
public class AboutActivity extends AboutActivityParent
{
	@Inject
	IVendorContract vendor;

	@Override
	protected void InitializeLayout() {
		ReleaseTextView().setText(getString(R.string.ReleaseComment, FarayanUtility.GetPackageInfo().versionName));
		AppIdTextView().setText(FarayanUtility.GetPackageName());

		RateAndReviewCommentTextView().setText(getString(R.string.RateAndReviewComment, getString(vendor.getNameResID())));
		RateAndReviewButton().setOnClickListener(RateAndReviewButtonOnClickListener());
		RateAndReviewCommentTextView().setVisibility(vendor.getSupportsRate() ? View.VISIBLE : View.GONE);
		RateAndReviewButton().setVisibility(vendor.getSupportsRate() ? View.VISIBLE : View.GONE);

		CheckButton().setOnClickListener(view -> updateCheck());
		CheckButton().setVisibility(vendor.getSupportsUpdate() ? View.VISIBLE : View.GONE);

		UpdateButton().setOnClickListener(UpdateButtonOnClickListener());
		CheckButton().setVisibility(vendor.getSupportsUpdate() ? View.VISIBLE : View.GONE);

		ShareButton().setOnClickListener(ShareButtonOnClickListener());
		CheckButton().setVisibility(vendor.getSupportsShare() ? View.VISIBLE : View.GONE);
		if (vendor.getSupportsUpdate())
			updateCheck();

		FeedbackButton().setOnClickListener(FeedbackButtonOnClickListener());

		TwitterIcon().setOnClickListener(view -> OpenUrl("https://twitter.com/SabadMarketApp"));
		FacebookIcon().setOnClickListener(view -> OpenUrl("https://facebook.com/SabadMarketApp"));
		EmailIcon().setOnClickListener(view -> OpenUrl("mailto:feedback@sabad.market"));
		WebsiteIcon().setOnClickListener(view -> OpenUrl("https://sabad.market"));
		TelegramSupportIcon().setOnClickListener(view -> OpenUrl("https://t.me/SabadSupport"));
		TelegramSubscribeChannelIcon().setOnClickListener(view -> OpenUrl("https://t.me/SabadMarketApp"));
		TelegramBrowseChannelIcon().setOnClickListener(view -> OpenUrl("https://t.me/s/SabadMarketApp"));
		InstagramIcon().setOnClickListener(view -> OpenUrl("https://instagram.com/SabadMarketApp"));
		WhatsAppSupportIcon().setOnClickListener(view -> OpenUrl("https://wa.me/989150863563"));
	}

	private void OpenUrl(String url) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.setData(Uri.parse(url));
		startActivity(intent);
	}

	private View.OnClickListener FeedbackButtonOnClickListener() {
		return view -> FeedbackLayout().setVisibility(FeedbackLayout().getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE);
	}

	private View.OnClickListener RateAndReviewButtonOnClickListener() {
		return view -> {
			vendor.rate(AboutActivity.this);
		};
	}

	private View.OnClickListener ShareButtonOnClickListener() {
		return view -> {
			Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
			shareIntent.setType("text/plain");
			String shareUrl = vendor.shareUrl();
			String line = "\n";
			String text = "من از اپ فهرست خرید سبد استفاده می‌کنم." + line
					+ "برای دریافت و نصب «سبد» لینک زیر را باز کن" + line + line
					+ shareUrl;
			shareIntent.putExtra(android.content.Intent.EXTRA_TEXT, text);
			Intent chooser = Intent.createChooser(shareIntent, "روش همرسانی و اشتراک‌گذاری را مشخص کنید");
			startActivity(chooser);
		};
	}

	private View.OnClickListener UpdateButtonOnClickListener() {
		return view -> vendor.installUpdate(
				AboutActivity.this,
				IVendorContract.UpdateTypes.ImmediateUpdate,
				VendorUpdatesPriorities.None,
				UpdateActivityResultCode,
				0,
				progress -> {
					UpdateProgressBar().setVisibility(View.VISIBLE);
					int percent = (int) FarayanUtility.DivideRound(
							progress.getBytesDownloaded(),
							progress.getTotalBytesToDownload(),
							0
					) * 100;
					UpdateProgressBar().setProgress(percent);
				},
				state -> {
					UpdateProgressBar().setVisibility(View.GONE);
					StatusTextView().setText(state.getCommentResID());
				},
				e -> {
					UpdateProgressBar().setVisibility(View.GONE);
					StatusTextView().setText(VendorUpdateStates.Failed.getCommentResID());
				}
		);
	}

	private void updateCheck() {
		UpdatableTextView().setVisibility(View.GONE);
		UpdateButton().setVisibility(View.GONE);
		CheckButton().setVisibility(View.GONE);
		CheckFailedTextView().setVisibility(View.GONE);
		CheckProgressBar().setVisibility(View.VISIBLE);

		vendor.checkUpdate(
				this,
				this::displayLatestVersion,
				exception -> {
					CheckProgressBar().setVisibility(View.GONE);
					CheckFailedTextView().setVisibility(View.VISIBLE);
					UpdateButton().setVisibility(View.GONE);
					CheckButton().setVisibility(View.VISIBLE);
				}
		);

	}

	private void displayLatestVersion(IVendorContract.LatestVersion latest) {
		CheckProgressBar().setVisibility(View.GONE);
		CheckFailedTextView().setVisibility(View.GONE);

		if (latest.getCode() > FarayanUtility.GetPackageVersion()) {
			UpdatableTextView().setText(getString(R.string.UpdatableComment, latest.getName()));
			UpdatableTextView().setVisibility(View.VISIBLE);
			UpdateButton().setVisibility(View.VISIBLE);
		}
	}

	@Override
	protected void onDestroy() {
		vendor.stopUpdateCheck(this);
		super.onDestroy();
	}
}
