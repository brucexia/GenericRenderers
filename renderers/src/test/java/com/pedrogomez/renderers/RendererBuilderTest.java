package com.pedrogomez.renderers;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pedrogomez.renderers.exception.NeedsPrototypesException;
import com.pedrogomez.renderers.exception.PrototypeNotFoundException;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.Assert.assertEquals;

/**
 * Test class created to check the correct behaviour of RendererBuilder
 *
 * @author Pedro Vicente Gómez Sánchez.
 */
@SuppressWarnings({"unchecked", "ResultOfObjectAllocationIgnored", "ConstantConditions"})
public class RendererBuilderTest {

    @Mock
    private View mockedConvertView;
    @Mock
    private ViewGroup mockedParent;
    @Mock
    private LayoutInflater mockedLayoutInflater;
    @Mock
    private Object mockedContent;
    @Mock
    private View mockedRendererView;

    @Before
    public void setUp() {
        initializeMocks();
        initializePrototypes();
    }

    @Test(expected = NeedsPrototypesException.class)
    public void shouldThrowNeedsPrototypeExceptionIfPrototypesIsNull() {
        new ObjectRendererBuilder(null);
    }

    @Test(expected = NeedsPrototypesException.class)
    public void shouldNotAcceptNullPrototypes() {
        RendererBuilder rendererBuilder = new RendererBuilder();

        rendererBuilder.withPrototypes(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldNotAcceptNullKeysBindingAPrototype() {
        RendererBuilder rendererBuilder = new RendererBuilder();

        rendererBuilder.bind(null, new ObjectRenderer());
    }

    @Test
    public void shouldAddPrototypeAndConfigureRendererBinding() {
        RendererBuilder rendererBuilder = new RendererBuilder();

        rendererBuilder.bind(Object.class, new ObjectRenderer());

        assertEquals(ObjectRenderer.class, rendererBuilder.getPrototypeClass(new Object()));
    }

    @Test
    public void shouldAddPrototypeAndConfigureRendererBindingForType() {
        int type = 1;
        RendererBuilder rendererBuilder = new RendererBuilder();

        rendererBuilder.bind(type, new ObjectRenderer());

        assertEquals(ObjectRenderer.class, rendererBuilder.getPrototypeClass(
                new RendererContent<>(new Object(), type)));
    }

    @Test(expected = PrototypeNotFoundException.class)
    public void shouldFailForWrongType() {
        int type = 1;
        int anotherType = 2;
        RendererBuilder rendererBuilder = new RendererBuilder();

        rendererBuilder.bind(type, new ObjectRenderer());
        rendererBuilder.bind(anotherType, new ObjectRenderer());

        rendererBuilder.getPrototypeClass(new RendererContent<>(new Object(), -1));
    }

    @Test
    public void shouldAddPrototyeAndConfigureBindingOnConstruction() {
        RendererBuilder rendererBuilder = new RendererBuilder(new ObjectRenderer());

        assertEquals(ObjectRenderer.class, rendererBuilder.getPrototypeClass(new Object()));
    }

    private void initializeMocks() {
        MockitoAnnotations.initMocks(this);
    }

    private void initializePrototypes() {
        ObjectRenderer objectRenderer = new ObjectRenderer();
        objectRenderer.setView(mockedRendererView);
        SubObjectRenderer subObjectRenderer = new SubObjectRenderer();
        subObjectRenderer.setView(mockedRendererView);
    }
}
