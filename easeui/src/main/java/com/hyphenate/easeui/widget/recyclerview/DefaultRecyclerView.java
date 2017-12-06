package com.hyphenate.easeui.widget.recyclerview;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import com.hyphenate.util.EMLog;

public class DefaultRecyclerView extends RecyclerView {
    private static final String TAG = "DefaultRecyclerView";

    private static final float DRAG_RATE = 2;

    private static final int TYPE_HEADER = 100000;
    private static final int TYPE_FOOTER = 100001;

    private final RecyclerView.AdapterDataObserver mDataObserver = new DataObserver();

    private WrapAdapter mWrapAdapter;
    private float mLastY = -1;
    private AbstractHeaderView mHeaderView;
    private AbstractHeaderView.HeaderListener mHeaderListener;

    public DefaultRecyclerView(Context context) {
        this(context, null);
    }

    public DefaultRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DefaultRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mWrapAdapter = new WrapAdapter(adapter);
        super.setAdapter(mWrapAdapter);
        mWrapAdapter.registerAdapterDataObserver(mDataObserver);
        mDataObserver.onChanged();
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mLastY == -1) {
            mLastY = ev.getRawY();
        }

        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = ev.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                final float deltaY = ev.getRawY() - mLastY;
                mLastY = ev.getRawY();
                if (isOnTop()) {
                    mHeaderView.onMove(deltaY / DRAG_RATE);
                    if (mHeaderView.getVisibleHeight() > 0 && mHeaderView.getState() < AbstractHeaderView.STATE_LOADING) {
                        return false;
                    }
                }
                break;
            default: // reset
                mLastY = -1;
                if (isOnTop()) {
                    if (mHeaderView.releaseAction()) {
                        if (mHeaderListener != null) {
                            mHeaderListener.onLoading();
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(ev);
    }

    @Override
    public void scrollToPosition(int position) {
        int adjPosition = position + getHeaderCount();
        if (getLayoutManager() instanceof LinearLayoutManager) {
            ((LinearLayoutManager) getLayoutManager()).scrollToPositionWithOffset(adjPosition, 0);
            return;
        }
        getLayoutManager().scrollToPosition(adjPosition);
    }

    public void setHeaderView(AbstractHeaderView headerView, @Progress.Style int style) {
        mHeaderView = headerView;
        mHeaderView.setProgressStyle(style);
    }

    public void setHeaderView(AbstractHeaderView headerView) {
        setHeaderView(headerView, Progress.SysProgress);
    }

    /**
     * Set the recyclerview a header view.
     *
     * @param header The header view 's layout id.
     * @param style  See {@link Progress}
     */
    public void setHeaderView(@LayoutRes int header, int style) {
        AbstractHeaderView headerView = (AbstractHeaderView) LayoutInflater.from(getContext())
                .inflate(header, null);
        setHeaderView(headerView, style);
    }

    public void setHeaderView(@LayoutRes int header) {
        setHeaderView(header, Progress.SysProgress);
    }

    public void setHeaderListener(AbstractHeaderView.HeaderListener listener) {
        mHeaderListener = listener;
    }

    // TODO: refresh the list content.
    public void refresh() {
        post(new Runnable() {
            @Override
            public void run() {

            }
        });
    }

    public void finishLoading() {
        if (mHeaderView == null) return;
        mHeaderView.loadingComplete();
    }

    public int getHeaderCount() {
        return mHeaderView != null ? 1 : 0;
    }

    private boolean isOnTop() {
        if (mHeaderView == null) {
            return false;
        }

        if (mHeaderView.getParent() != null) {
            return true;
        } else {
            return false;
        }
    }

    //判断是否是SuperRecyclerView保留的itemViewType
    private boolean isReservedItemViewType(int itemViewType) {
        if (itemViewType == TYPE_HEADER || itemViewType == TYPE_FOOTER) {
            return true;
        } else {
            return false;
        }
    }

    private class DataObserver extends RecyclerView.AdapterDataObserver {
        @Override
        public void onChanged() {
            if (mWrapAdapter != null) {
                mWrapAdapter.notifyDataSetChanged();
            }
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mWrapAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mWrapAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mWrapAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    }

    private class WrapAdapter extends Adapter<ViewHolder> {
        private Adapter adapter;

        public WrapAdapter(Adapter adapter) {
            this.adapter = adapter;
        }

        public boolean isHeader(int position) {
            return getHeaderCount() > 0 && position == 0;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if (viewType == TYPE_HEADER) {
                return new SimpleViewHolder(mHeaderView);
            }
            return adapter.onCreateViewHolder(parent, viewType);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            if (isHeader(position)) {
                return;
            }
            int adjPosition = position - getHeaderCount();
            int adapterCount;
            if (adapter != null) {
                adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    adapter.onBindViewHolder(holder, adjPosition);
                    return;
                }
            }
        }

        @Override
        public int getItemCount() {
            if (adapter == null) return getHeaderCount();
            return adapter.getItemCount() + getHeaderCount();
        }

        @Override
        public int getItemViewType(int position) {
            int adjPosition = position - getHeaderCount();
            if (adjPosition > -1 && isReservedItemViewType(adapter.getItemViewType(adjPosition))) {
                throw new IllegalStateException("XRecyclerView require itemViewType in adapter should be less than 10000 ");
            }
            if (isHeader(position)) {
                return TYPE_HEADER;
            }

            if (adapter != null) {
                int adapterCount = adapter.getItemCount();
                if (adjPosition < adapterCount) {
                    return adapter.getItemViewType(adjPosition);
                }
            }
            return 0;
        }

        @Override
        public long getItemId(int position) {
            if (adapter != null && position >= getHeaderCount()) {
                int adjPosition = position - getHeaderCount();
                if (adjPosition < adapter.getItemCount()) {
                    return adapter.getItemId(adjPosition);
                }
            }
            return -1;
        }


        @Override
        public void onAttachedToRecyclerView(RecyclerView recyclerView) {
            super.onAttachedToRecyclerView(recyclerView);
            LayoutManager manager = recyclerView.getLayoutManager();
            if (manager instanceof GridLayoutManager) {
                final GridLayoutManager gridManager = ((GridLayoutManager) manager);
                gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                    @Override
                    public int getSpanSize(int position) {
                        return (isHeader(position) || isHeader(position))
                                ? gridManager.getSpanCount() : 1;
                    }
                });
            }
            adapter.onAttachedToRecyclerView(recyclerView);
        }

        @Override
        public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
            adapter.onDetachedFromRecyclerView(recyclerView);
        }

        @Override
        public void onViewAttachedToWindow(ViewHolder holder) {
            EMLog.i(TAG, "onViewAttachedToWindow: ");
            super.onViewAttachedToWindow(holder);
            ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
            if (lp != null
                    && lp instanceof StaggeredGridLayoutManager.LayoutParams
                    && (isHeader(holder.getLayoutPosition()) || isHeader(holder.getLayoutPosition()))) {
                StaggeredGridLayoutManager.LayoutParams p = (StaggeredGridLayoutManager.LayoutParams) lp;
                p.setFullSpan(true);
            }
            adapter.onViewAttachedToWindow(holder);
        }

        @Override
        public void onViewDetachedFromWindow(ViewHolder holder) {
            EMLog.i(TAG, "onViewDetachedFromWindow: ");
            if (holder instanceof SimpleViewHolder) return;
            adapter.onViewDetachedFromWindow(holder);
        }

        @Override
        public void onViewRecycled(ViewHolder holder) {
            adapter.onViewRecycled(holder);
        }

        @Override
        public boolean onFailedToRecycleView(ViewHolder holder) {
            return adapter.onFailedToRecycleView(holder);
        }

        @Override
        public void unregisterAdapterDataObserver(AdapterDataObserver observer) {
            adapter.unregisterAdapterDataObserver(observer);
        }

        @Override
        public void registerAdapterDataObserver(AdapterDataObserver observer) {
            adapter.registerAdapterDataObserver(observer);
        }

        private class SimpleViewHolder extends ViewHolder {
            public SimpleViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}