package farayan.sabad.ui;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.Objects;

import farayan.commons.FarayanUtility;
import farayan.commons.UI.Core.IGenericEvent;
import farayan.sabad.R;
import farayan.sabad.core.OnePlace.Group.DuplicatedNameRexception;
import farayan.sabad.core.OnePlace.Group.EmptyNameRexception;
import farayan.sabad.core.OnePlace.Group.GroupEntity;
import farayan.sabad.core.OnePlace.Group.IGroupRepo;
import farayan.sabad.core.OnePlace.GroupUnit.IGroupUnitRepo;
import farayan.sabad.core.model.product.IProductRepo;

public class GroupFormDialog extends GroupFormDialogParent {
    private final Input TheInput;

    public GroupFormDialog(
            @NonNull Input input,
            @NonNull Activity context
    ) {
        super(context);
        TheInput = input;
        Reload();
    }

    @Override
    protected void InitializeLayout() {
        NameEditText().setOnEditorActionListener(NameEditTextOnEditorActionListener());
        PersistButton().setOnClickListener(PersistButtonOnClickListener());
        RemoveButton().setOnClickListener(RemoveButtonOnClickListener());
        RemoveButton().setOnLongClickListener(RemoveButtonOnLongClickListener());
        super.InitializeLayout();
    }

    private TextView.OnEditorActionListener NameEditTextOnEditorActionListener() {
        return (textView, actionId, keyEvent) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                Persist();
                return true;
            }
            return false;
        };
    }

    private View.OnLongClickListener RemoveButtonOnLongClickListener() {
        return view -> {
            TheInput.TypeRepo.Remove(
                    TheInput.ProductRepo,
                    TheInput.TypeUnitRepo,
                    TheInput.Type
            );
            FarayanUtility.ShowToastFormatted(
                    TheActivity,
                    TheActivity.getString(R.string.PurchasableRemoved),
                    TheInput.Type.DisplayableName
            );
            if (TheInput.OnRemoved != null)
                TheInput.OnRemoved.Fire(TheInput.Type);
            dismiss();
            return true;
        };
    }

    private View.OnClickListener RemoveButtonOnClickListener() {
        return view -> {
            FarayanUtility.ShowToastFormatted(TheActivity, "برای حذف خریدنی، انگشت خود را برروی دکمه‌ی حذف نگه دارید");
        };
    }

    private View.OnClickListener PersistButtonOnClickListener() {
        return view -> {
            Persist();
        };
    }

    private void Persist() {
        try {
            TheInput.TypeRepo.Rename(TheInput.Type, Objects.requireNonNull(NameEditText().getText()).toString());
            IGenericEvent.Exec(TheInput.OnRenamed, TheInput.Type);
            dismiss();
        } catch (EmptyNameRexception exception) {
            NameTextLayout().setError("نام را درج کنید");
        } catch (DuplicatedNameRexception exception) {
            NameTextLayout().setError("نام تکراری است، نام دیگری درج کنید");
        }
    }

    private void Reload() {
        NameEditText().setText(TheInput.Type.DisplayableName);
    }

    static class Input {
        private final GroupEntity Type;
        private final IGroupRepo TypeRepo;
        private final IProductRepo ProductRepo;
        private final IGroupUnitRepo TypeUnitRepo;
        private final IGenericEvent<GroupEntity> OnRenamed;
        private final IGenericEvent<GroupEntity> OnRemoved;

        public Input(
                @NonNull GroupEntity type,
                @NonNull IGroupRepo typeRepo,
                @NonNull IProductRepo productRepo,
                @NonNull IGroupUnitRepo typeUnitRepo,
                @NonNull IGenericEvent<GroupEntity> onRenamed,
                @NonNull IGenericEvent<GroupEntity> onRemoved
        ) {
            Type = type;
            TypeRepo = typeRepo;
            ProductRepo = productRepo;
            TypeUnitRepo = typeUnitRepo;
            OnRenamed = onRenamed;
            OnRemoved = onRemoved;
        }
    }
}
