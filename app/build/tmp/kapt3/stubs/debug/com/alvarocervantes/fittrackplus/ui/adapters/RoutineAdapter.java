package com.alvarocervantes.fittrackplus.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.alvarocervantes.fittrackplus.data.preferences.LastRoutineManager;
import com.alvarocervantes.fittrackplus.R;
import com.alvarocervantes.fittrackplus.data.model.RoutineEntity;

@kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u00004\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010 \n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\u0010\b\n\u0000\n\u0002\u0018\u0002\n\u0002\u0010\u0002\n\u0002\b\b\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\f\u0012\b\u0012\u00060\u0002R\u00020\u00000\u0001:\u0001\u0015BG\u0012\u0018\u0010\u0003\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u00050\u0004\u0012\u0012\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n0\t\u0012\u0012\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n0\t\u00a2\u0006\u0002\u0010\fJ\b\u0010\r\u001a\u00020\u0007H\u0016J\u001c\u0010\u000e\u001a\u00020\n2\n\u0010\u000f\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0010\u001a\u00020\u0007H\u0016J\u001c\u0010\u0011\u001a\u00060\u0002R\u00020\u00002\u0006\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u0007H\u0016R\u001a\u0010\u000b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u001a\u0010\b\u001a\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\n0\tX\u0082\u0004\u00a2\u0006\u0002\n\u0000R \u0010\u0003\u001a\u0014\u0012\u0010\u0012\u000e\u0012\u0004\u0012\u00020\u0006\u0012\u0004\u0012\u00020\u00070\u00050\u0004X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u0016"}, d2 = {"Lcom/alvarocervantes/fittrackplus/ui/adapters/RoutineAdapter;", "Landroidx/recyclerview/widget/RecyclerView$Adapter;", "Lcom/alvarocervantes/fittrackplus/ui/adapters/RoutineAdapter$RoutineViewHolder;", "routines", "", "Lkotlin/Pair;", "Lcom/alvarocervantes/fittrackplus/data/model/RoutineEntity;", "", "onRoutineClick", "Lkotlin/Function1;", "", "onEditClick", "(Ljava/util/List;Lkotlin/jvm/functions/Function1;Lkotlin/jvm/functions/Function1;)V", "getItemCount", "onBindViewHolder", "holder", "position", "onCreateViewHolder", "parent", "Landroid/view/ViewGroup;", "viewType", "RoutineViewHolder", "app_debug"})
public final class RoutineAdapter extends androidx.recyclerview.widget.RecyclerView.Adapter<com.alvarocervantes.fittrackplus.ui.adapters.RoutineAdapter.RoutineViewHolder> {
    @org.jetbrains.annotations.NotNull
    private final java.util.List<kotlin.Pair<com.alvarocervantes.fittrackplus.data.model.RoutineEntity, java.lang.Integer>> routines = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<com.alvarocervantes.fittrackplus.data.model.RoutineEntity, kotlin.Unit> onRoutineClick = null;
    @org.jetbrains.annotations.NotNull
    private final kotlin.jvm.functions.Function1<com.alvarocervantes.fittrackplus.data.model.RoutineEntity, kotlin.Unit> onEditClick = null;
    
    public RoutineAdapter(@org.jetbrains.annotations.NotNull
    java.util.List<kotlin.Pair<com.alvarocervantes.fittrackplus.data.model.RoutineEntity, java.lang.Integer>> routines, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.alvarocervantes.fittrackplus.data.model.RoutineEntity, kotlin.Unit> onRoutineClick, @org.jetbrains.annotations.NotNull
    kotlin.jvm.functions.Function1<? super com.alvarocervantes.fittrackplus.data.model.RoutineEntity, kotlin.Unit> onEditClick) {
        super();
    }
    
    @java.lang.Override
    @org.jetbrains.annotations.NotNull
    public com.alvarocervantes.fittrackplus.ui.adapters.RoutineAdapter.RoutineViewHolder onCreateViewHolder(@org.jetbrains.annotations.NotNull
    android.view.ViewGroup parent, int viewType) {
        return null;
    }
    
    @java.lang.Override
    public void onBindViewHolder(@org.jetbrains.annotations.NotNull
    com.alvarocervantes.fittrackplus.ui.adapters.RoutineAdapter.RoutineViewHolder holder, int position) {
    }
    
    @java.lang.Override
    public int getItemCount() {
        return 0;
    }
    
    @kotlin.Metadata(mv = {1, 9, 0}, k = 1, xi = 48, d1 = {"\u0000\"\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0002\b\u0005\b\u0086\u0004\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0011\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0007\u0010\bR\u0011\u0010\t\u001a\u00020\u0003\u00a2\u0006\b\n\u0000\u001a\u0004\b\n\u0010\u000bR\u0011\u0010\f\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u000e\u0010\u000fR\u0011\u0010\u0010\u001a\u00020\r\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u000f\u00a8\u0006\u0012"}, d2 = {"Lcom/alvarocervantes/fittrackplus/ui/adapters/RoutineAdapter$RoutineViewHolder;", "Landroidx/recyclerview/widget/RecyclerView$ViewHolder;", "view", "Landroid/view/View;", "(Lcom/alvarocervantes/fittrackplus/ui/adapters/RoutineAdapter;Landroid/view/View;)V", "buttonEdit", "Landroid/widget/Button;", "getButtonEdit", "()Landroid/widget/Button;", "cardView", "getCardView", "()Landroid/view/View;", "textDays", "Landroid/widget/TextView;", "getTextDays", "()Landroid/widget/TextView;", "textName", "getTextName", "app_debug"})
    public final class RoutineViewHolder extends androidx.recyclerview.widget.RecyclerView.ViewHolder {
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView textName = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.TextView textDays = null;
        @org.jetbrains.annotations.NotNull
        private final android.widget.Button buttonEdit = null;
        @org.jetbrains.annotations.NotNull
        private final android.view.View cardView = null;
        
        public RoutineViewHolder(@org.jetbrains.annotations.NotNull
        android.view.View view) {
            super(null);
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTextName() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.TextView getTextDays() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.widget.Button getButtonEdit() {
            return null;
        }
        
        @org.jetbrains.annotations.NotNull
        public final android.view.View getCardView() {
            return null;
        }
    }
}