package com.hyphenate.easeui.widget.recyclerview.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

public abstract class EaseBaseAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private static final String TAG = "AbstractAdapter";

    /**
     * Base config
     */
    private Context mContext;

    /**
     * Listener
     */
    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mOnItemLongClickListener;
    private OnRecyclerViewItemChildClickListener mChildClickListener;
    private OnRecyclerViewItemChildLongClickListener mChildLongClickListener;

    public EaseBaseAdapter(Context context) {
        mContext = context;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        BaseViewHolder baseViewHolder = new BaseViewHolder(onCreateView(parent, viewType), mContext);
        initItemClickListener(baseViewHolder);
        return baseViewHolder;
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        onBindView(holder, position);
    }

    @Override
    public abstract int getItemViewType(int position);

    /**
     * init the baseViewHolder to register mOnItemClickListener and mOnItemLongClickListener
     *
     * @param holder
     */
    protected final void initItemClickListener(final BaseViewHolder holder) {
        if (null != mOnItemClickListener) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final int position = holder.getAdapterPosition() - 1;
                    if (position < 0 || position > getItemCount() - 1) {
                        return;
                    }
                    mOnItemClickListener.onItemClick(view, position);
                }
            });
        }

        if (null != mOnItemLongClickListener) {
            holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    final int position = holder.getAdapterPosition() - 1;
                    if (position < 0 || position > getItemCount() - 1) {
                        return true;
                    }
                    mOnItemLongClickListener.onItemLongClick(v, position);
                    return true;
                }
            });
        }
    }

    /**
     * Base api
     */
    protected abstract View onCreateView(ViewGroup parent, int viewType);

    protected abstract void onBindView(BaseViewHolder holder, int position);

    /**
     * Listener api
     */
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        mOnItemLongClickListener = onItemLongClickListener;
    }

    /**
     * Register a callback to be invoked when childView in this AdapterView has
     * been clicked and held
     * {@link OnRecyclerViewItemChildClickListener}
     *
     * @param childClickListener The callback that will run
     */
    public void setOnItemChildClickListener(OnRecyclerViewItemChildClickListener childClickListener) {
        this.mChildClickListener = childClickListener;
    }

    public class OnItemChildClickListener implements View.OnClickListener {
        public RecyclerView.ViewHolder mViewHolder;

        @Override
        public void onClick(View v) {
            if (mChildClickListener != null) {
                int position = mViewHolder.getLayoutPosition() - 1;
                mChildClickListener.onItemChildClick(EaseBaseAdapter.this, v, position);
            }
        }
    }

    public class OnItemChildLongClickListener implements View.OnLongClickListener {
        public RecyclerView.ViewHolder mViewHolder;

        @Override
        public boolean onLongClick(View v) {
            if (mChildLongClickListener != null) {
                return mChildLongClickListener.onItemChildLongClick(EaseBaseAdapter.this, v, mViewHolder.getLayoutPosition() - 1);
            }
            return false;
        }
    }

    /**
     * Register a callback to be invoked when childView in this AdapterView has
     * been longClicked and held
     * {@link OnRecyclerViewItemChildLongClickListener}
     *
     * @param childLongClickListener The callback that will run
     */
    public void setOnItemChildLongClickListener(OnRecyclerViewItemChildLongClickListener childLongClickListener) {
        this.mChildLongClickListener = childLongClickListener;
    }

    /**
     * Some interface
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

    public interface OnRecyclerViewItemChildClickListener {
        void onItemChildClick(EaseBaseAdapter adapter, View view, int position);
    }

    public interface OnRecyclerViewItemChildLongClickListener {
        boolean onItemChildLongClick(EaseBaseAdapter adapter, View view, int position);
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
}
