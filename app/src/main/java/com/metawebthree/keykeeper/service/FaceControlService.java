package com.metawebthree.keykeeper.service;

import android.accessibilityservice.AccessibilityService;
import android.accessibilityservice.GestureDescription;
import android.graphics.Path;
import android.view.accessibility.AccessibilityEvent;

import com.google.mediapipe.framework.Graph;

public class FaceControlService extends AccessibilityService {
    private Graph mediapipeGraph;

    @Override
    protected void onServiceConnected() {
        mediapipeGraph = new Graph();
    }

    @Override
    public void onAccessibilityEvent(AccessibilityEvent event) {
        // 获取人脸识别结果
        /*
         faceAction = event.getAction();

        // 根据人脸动作进行设备操作

        // 示例：根据人脸左右摇头进行滑动操作
        if (faceAction == FaceAction.LEFT) {
            performSwipeLeft();
        } else if (faceAction == FaceAction.RIGHT) {
            performSwipeRight();
        }*/
    }

    private void performSwipeLeft() {
        // 创建滑动手势路径
        Path swipePath = new Path();
        swipePath.moveTo(500, 500);
        swipePath.lineTo(100, 500);

        // 创建滑动手势描述
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        gestureBuilder.addStroke(new GestureDescription.StrokeDescription(swipePath, 0, 500));

        // 分发滑动手势
        dispatchGesture(gestureBuilder.build(), null, null);
    }

    private void performSwipeRight() {
        // 创建滑动手势路径
        Path swipePath = new Path();
        swipePath.moveTo(100, 500);
        swipePath.lineTo(500, 500);

        // 创建滑动手势描述
        GestureDescription.Builder gestureBuilder = new GestureDescription.Builder();
        gestureBuilder.addStroke(new GestureDescription.StrokeDescription(swipePath, 0, 500));

        // 分发滑动手势
        dispatchGesture(gestureBuilder.build(), null, null);
    }

    @Override
    public void onInterrupt() {
        // 中断处理
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 停止Mediapipe图形
        mediapipeGraph.cancelGraph();
    }
}
