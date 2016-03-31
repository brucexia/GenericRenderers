package com.pedrogomez.renderers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Generic renderer to easily wrap Views for being used in the adapter.
 *
 * @param <T> The type of the content used by the view.
 * @param <U> The View type.
 * @author alberto.ballano
 */
@SuppressWarnings("unused")
public class ViewRenderer<T, U extends View> extends Renderer<T> {
    private final InflateFunction<U> initFunc;
    private final RenderFunction<T, U> renderFunc;
    private U view;

    /**
     * @param initFunc Function for the inflate process.
     * @param renderFunc Function for the render process.
     */
    public ViewRenderer(InflateFunction<U> initFunc, RenderFunction<T, U> renderFunc) {
        this.initFunc = initFunc;
        this.renderFunc = renderFunc;
    }

    @Override
    protected View inflate(LayoutInflater inflater, ViewGroup parent) {
        return initFunc.inflate(parent.getContext());
    }

    @Override
    public void render(List<Object> payloads) {
        //noinspection unchecked
        renderFunc.render(getContent(), (U) getRootView());
    }

    /** Inflate function interface */
    public interface InflateFunction<V extends View> {
        /** Called when the inflation is taking place. Should return the inflated view. */
        V inflate(Context context);
    }

    /** Render function interface */
    public interface RenderFunction<W, X extends View> {
        /** Called when the render is taking place. Should setup the view. */
        void render(W w, X x);
    }
}
