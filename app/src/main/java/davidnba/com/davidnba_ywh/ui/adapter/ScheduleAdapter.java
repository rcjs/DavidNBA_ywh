package davidnba.com.davidnba_ywh.ui.adapter;

import android.content.Context;
import android.view.View;


import com.facebook.drawee.view.SimpleDraweeView;
import com.zengcanxiang.baseAdapter.recyclerView.HelperAdapter;
import com.zengcanxiang.baseAdapter.recyclerView.HelperViewHolder;

import java.util.List;

import davidnba.com.davidnba_ywh.R;
import davidnba.com.davidnba_ywh.http.bean.match.Matchs;
import davidnba.com.davidnba_ywh.support.NoDoubleClickListener;
import davidnba.com.davidnba_ywh.support.OnListItemClickListener;
import davidnba.com.davidnba_ywh.utils.FrescoUtils;
import davidnba.com.davidnba_ywh.utils.ItemAnimHelper;

/**
 * Created by 仁昌居士 on 2017/2/16.
 */

public class ScheduleAdapter extends HelperAdapter<Matchs.MatchsDataBean.MatchesBean> {
    private OnListItemClickListener mOnItemClickListener = null;
    private ItemAnimHelper helper = new ItemAnimHelper();

    public ScheduleAdapter(List<Matchs.MatchsDataBean.MatchesBean> mList, Context context, int... layoutIds) {
        super(mList, context, layoutIds);
    }

    @Override
    protected void HelperBindData(final HelperViewHolder viewHolder, final int position, final Matchs.MatchsDataBean.MatchesBean item) {
        Matchs.MatchsDataBean.MatchesBean.MatchInfoBean matchInfo = item.matchInfo;

        SimpleDraweeView ivLeft = viewHolder.getView(R.id.ivLeftTeam);
        ivLeft.setController(FrescoUtils.getController(matchInfo.leftBadge, ivLeft));
        SimpleDraweeView ivRight = viewHolder.getView(R.id.ivRightTeam);
        ivRight.setController(FrescoUtils.getController(matchInfo.rightBadge, ivRight));

        String status;
        if (((matchInfo.quarter.contains("第4节") || matchInfo.quarter.contains("加时")) && !matchInfo.leftGoal.equals(matchInfo.rightGoal))
                && matchInfo.quarterTime.contains("00:00")) {
            status = "已结束";
        } else if (matchInfo.quarter.equals("") && matchInfo.quarterTime.equals("12:00")) {
            status = matchInfo.startTime;
        } else {
            status = matchInfo.quarter + " " + matchInfo.quarterTime;
        }
        String broadcasters = "";
        if (matchInfo.broadcasters != null) {
            for (String str : matchInfo.broadcasters) {
                broadcasters += str;
            }
        }
        viewHolder.setText(R.id.tvLeftTeam, matchInfo.leftName)
                .setText(R.id.tvRightTeam, matchInfo.rightName)
                .setText(R.id.tvMatchStatus, status)
                .setText(R.id.tvLeftTeamPoint, matchInfo.leftGoal)
                .setText(R.id.tvRightTeamPoint, matchInfo.rightGoal)
                .setText(R.id.tvMatchDesc, matchInfo.matchDesc)
                .setText(R.id.tvBroadcasters, broadcasters);

        viewHolder.getItemView().setOnClickListener(new NoDoubleClickListener() {
            @Override
            protected void onNoDoubleClick(View view) {
                if (mOnItemClickListener != null)
                    mOnItemClickListener.onItemClick(viewHolder.getItemView(), position, item);
            }
        });

        helper.showItemAnim(viewHolder.getItemView(), position);
    }

    public void setOnItemClickListener(OnListItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }
}
