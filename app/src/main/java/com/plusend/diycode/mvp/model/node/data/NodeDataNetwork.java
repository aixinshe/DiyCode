package com.plusend.diycode.mvp.model.node.data;

import android.util.Log;
import com.plusend.diycode.mvp.model.node.entity.Node;
import com.plusend.diycode.mvp.model.node.event.NodesEvent;
import java.util.List;
import org.greenrobot.eventbus.EventBus;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.GsonConverterFactory;
import retrofit2.Response;
import retrofit2.Retrofit;

public class NodeDataNetwork implements NodeData {
  private static final String TAG = "NetworkData";
  private NodeService service;
  private static NodeDataNetwork networkData = new NodeDataNetwork();

  private NodeDataNetwork() {
    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://www.diycode.cc/api/v3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build();
    service = retrofit.create(NodeService.class);
  }

  public static NodeDataNetwork getInstance() {
    return networkData;
  }

  @Override public void readNodes() {
    Call<List<Node>> call = service.readNodes();
    call.enqueue(new Callback<List<Node>>() {
      @Override public void onResponse(Call<List<Node>> call, Response<List<Node>> response) {
        if (response.isSuccessful()) {
          List<Node> nodeList = response.body();
          Log.v(TAG, "nodeList:" + nodeList);
          EventBus.getDefault().post(new NodesEvent(nodeList));
        } else {
          Log.e(TAG, "readNodes STATUS: " + response.code());
          EventBus.getDefault().post(new NodesEvent(null));
        }
      }

      @Override public void onFailure(Call<List<Node>> call, Throwable t) {
        Log.e(TAG, t.getMessage());
        EventBus.getDefault().post(new NodesEvent(null));
      }
    });
  }
}
