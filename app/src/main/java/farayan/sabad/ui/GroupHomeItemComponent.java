package farayan.sabad.ui;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import java.math.BigDecimal;
import java.util.Objects;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import farayan.commons.Commons.Rial;
import farayan.commons.FarayanUtility;
import farayan.commons.UI.Core.IEntityView;
import farayan.commons.UI.Core.IGenericEvent;
import farayan.sabad.R;
import farayan.sabad.SabadUtility;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo;
import farayan.sabad.core.OnePlace.InvoiceItem.IInvoiceItemRepo;
import farayan.sabad.core.OnePlace.NeedChange.INeedChangeRepo;
import farayan.sabad.core.OnePlace.Unit.IUnitRepo;
import farayan.sabad.core.model.product.IProductRepo;

@AndroidEntryPoint
public class GroupHomeItemComponent extends GroupHomeItemComponentParent implements IEntityView<GroupEntity> {
    private final IGenericEvent<GroupEntity> OnNeededChanged;
    private final IGenericEvent<GroupEntity> OnPickedChanged;
    private final IGenericEvent<GroupHomeItemComponent> OnPicked;
    private final IGenericEvent<GroupEntity> OnRemoved;
    private final Rial.Coefficients RialFixedCoefficient;
    private IGroupRepo TheGroupRepo;
    private IInvoiceItemRepo TheInvoiceItemRepo;
    private IProductRepo TheProductRepo;
    private IUnitRepo TheUnitRepo;
    private IGroupUnitRepo TheGroupUnitRepo;
    private INeedChangeRepo TheNeedChangeRepo;
    private GroupEntity TheEntity;

    public GroupHomeItemComponent(Context context) {
        super(context);
        OnNeededChanged = null;
        OnPickedChanged = null;
        OnPicked = null;
        OnRemoved = null;
        RialFixedCoefficient = null;
    }

    public GroupHomeItemComponent(Context context, AttributeSet attrs) {
        super(context, attrs);
        OnNeededChanged = null;
        OnPickedChanged = null;
        OnPicked = null;
        OnRemoved = null;
        RialFixedCoefficient = null;
    }

    public GroupHomeItemComponent(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        OnNeededChanged = null;
        OnPickedChanged = null;
        OnPicked = null;
        OnRemoved = null;
        RialFixedCoefficient = null;
    }

    public GroupHomeItemComponent(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        OnNeededChanged = null;
        OnPickedChanged = null;
        OnPicked = null;
        OnRemoved = null;
        RialFixedCoefficient = null;
    }

    public GroupHomeItemComponent(
            Context context,
            IGenericEvent<GroupEntity> onNeededChanged,
            IGenericEvent<GroupEntity> onPickedChanged,
            IGenericEvent<GroupHomeItemComponent> onPicked,
            IGenericEvent<GroupEntity> onRemoved,
            Rial.Coefficients rialFixedCoefficient
    ) {
        super(context);
        OnNeededChanged = onNeededChanged;
        OnPickedChanged = onPickedChanged;
        OnPicked = onPicked;
        OnRemoved = onRemoved;
        RialFixedCoefficient = rialFixedCoefficient;
    }

    @Inject
    public void Inject(
            IGroupRepo GroupRepo,
            IInvoiceItemRepo invoiceItemRepo,
            IProductRepo productRepo,
            IUnitRepo UnitRepo,
            IGroupUnitRepo GroupUnitRepo,
            INeedChangeRepo needChangeRepo
    ) {
        TheGroupRepo = GroupRepo;
        TheInvoiceItemRepo = invoiceItemRepo;
        TheProductRepo = productRepo;
        TheUnitRepo = UnitRepo;
        TheGroupUnitRepo = GroupUnitRepo;
        TheNeedChangeRepo = needChangeRepo;
    }

    public GroupEntity getEntity() {
        return TheEntity;
    }

    @Override
    protected void InitializeComponents() {
        NeededSwitch().setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (TheEntity == null)
                throw new RuntimeException();
            if (TheEntity.Needed == isChecked)
                return;
            TheNeedChangeRepo.RegisterChange(TheEntity, isChecked);
            TheEntity.Needed = isChecked;
            TheEntity.Picked = TheEntity.Item != null || (TheEntity.Needed && TheEntity.Picked);
            TheGroupRepo.Update(TheEntity);
            if (SabadUtility.NeedChangeGuideNeeded(getContext())) {
                FarayanUtility.ShowToastFormatted(getContext(), false, "راهنما* اگر به کالایی نیاز دارید، این سوئیچ را روشن و اگر نیاز ندارید، خاموش کنید");
            }
            DisplayEntity(TheEntity);
            if (OnNeededChanged != null)
                OnNeededChanged.Fire(TheEntity);
        });

        PickImageView().setOnClickListener(v -> {
            TheEntity.Picked = !TheEntity.Picked;
            TheGroupRepo.Update(TheEntity);
            if (SabadUtility.PickChangeGuideNeeded(getContext())) {
                FarayanUtility.ShowToastFormatted(getContext(), false, "راهنما* شما هم می‌توانید جزئیات کالای برداشته‌شده را ثبت کنید، هم با زدن این تیک، برداشت کالا را ثبت کنید");
            }
            DisplayEntity(TheEntity);
            if (OnPickedChanged != null)
                OnPickedChanged.Fire(TheEntity);
        });

        Container().setOnClickListener(Clicked());
        NameTextView().setOnClickListener(Clicked());

        NameTextView().setOnLongClickListener(view -> {
            GroupFormDialog dialog = new GroupFormDialog(
                    new GroupFormDialog.Input(
                            TheEntity,
                            TheGroupRepo,
                            TheInvoiceItemRepo,
                            TheProductRepo,
                            TheGroupUnitRepo,
                            g -> DisplayEntity(g.Refreshed(TheGroupRepo)),
                            g -> IGenericEvent.Exec(OnRemoved, g)
                    ),
                    (Activity) getContext()
            );
            dialog.show();
            return true;
        });

        super.InitializeComponents();
    }

    private OnClickListener Clicked() {
        return view -> {
            if (SabadUtility.GroupNameClickGuideNeeded(getContext())) {
                FarayanUtility.ShowToastFormatted(getContext(), false, "راهنما* با کلیک اول برروی کالای موجود، وضعیت آن به نیازمندی تغییر می‌کند. اگر کالا در فهرست خرید و نیازمندی باشد، کلیک برروی آن، پنجره‌ی ثبت اختیاری کالای برداشته را نمایش می‌دهد");
            }

            if (!TheEntity.Needed) {
                TheEntity.Needed = true;
                TheGroupRepo.Update(TheEntity);
                DisplayEntity(TheEntity);
            } else {
                IGenericEvent.Exec(OnPicked, GroupHomeItemComponent.this);
            }
        };
    }

    @Override
    public void DisplayEntity(GroupEntity entity) {
        TheEntity = entity.Refreshed(TheGroupRepo);
        NameTextView().setText(entity.DisplayableName);
        NeededSwitch().setChecked(entity.Needed);

        if (entity.Needed) {
            Container().setBackgroundResource(entity.Picked ? R.drawable.groups_item_container_bg_picked_shape : R.drawable.groups_item_container_bg_needed_shape);
            StatusTextView().setText(entity.Picked ? R.string.GroupStatusPicked : R.string.GroupStatusNeeded);
        } else {
            Container().setBackgroundResource(R.drawable.groups_item_container_bg_optional_shape);
            StatusTextView().setText(R.string.GroupStatusNone);
        }

        PickImageView().setVisibility(TheEntity.Needed ? VISIBLE : INVISIBLE);
        PickImageView().setImageResource(
                entity.Picked
                        ? R.drawable.ic_baseline_check_box_24
                        : R.drawable.ic_baseline_crop_square_24
        );
        if (entity.Item == null || !entity.Picked) {
            InvoiceItemSummaryTextView().setText("");
            InvoiceItemSummaryTextView().setVisibility(GONE);
        } else {
            if (Objects.equals(entity.Item.Refreshed(TheInvoiceItemRepo).Quantity, BigDecimal.ONE)) {
                String summary = "%s %s";
                summary = String.format(
                        summary,
                        FarayanUtility.CatchException(() -> FarayanUtility.Or(entity.Item.Product.Refreshed(TheProductRepo).DisplayableName, ""), x -> ""),
                        new Rial(RialFixedCoefficient, entity.Item.Total.doubleValue()).Textual(getContext().getResources())
                );
                InvoiceItemSummaryTextView().setText(summary);
                InvoiceItemSummaryTextView().setVisibility(VISIBLE);
            } else {
                String summary = "%s %s %s، فی: %s، جمع: %s";
                summary = String.format(
                        summary,
                        FarayanUtility.MoneyFormatted(entity.Item.Quantity.doubleValue(), true, false),
                        FarayanUtility.CatchException(() -> FarayanUtility.Or(entity.Item.Unit.Refreshed(TheUnitRepo).getDisplayableName(), "«واحد»"), x -> "«واحد»"),
                        FarayanUtility.CatchException(() -> FarayanUtility.Or(entity.Item.Product.Refreshed(TheProductRepo).DisplayableName, ""), x -> ""),
                        new Rial(RialFixedCoefficient, entity.Item.Fee.doubleValue()).Textual(getContext().getResources()),
                        new Rial(RialFixedCoefficient, entity.Item.Total.doubleValue()).Textual(getContext().getResources())
                );
                InvoiceItemSummaryTextView().setText(summary);
                InvoiceItemSummaryTextView().setVisibility(VISIBLE);
            }
        }
    }
}
