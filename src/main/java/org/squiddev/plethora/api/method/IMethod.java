package org.squiddev.plethora.api.method;

import dan200.computercraft.api.lua.ILuaContext;
import dan200.computercraft.api.lua.LuaException;
import org.squiddev.plethora.api.Injects;
import org.squiddev.plethora.api.method.wrapper.PlethoraMethod;

import javax.annotation.Nonnull;

/**
 * A Lua side method targeting a class.
 *
 * There are several ways of using {@link IMethod}:
 * - Extend {@link BasicMethod}, one of its subclasses or write your own. Then register using {@link Injects}.
 * - Create a static method on a class and register using {@link PlethoraMethod} or similar.
 */
public interface IMethod<T> {
	/**
	 * The name of this method
	 *
	 * @return The name of this method
	 */
	@Nonnull
	String getName();

	/**
	 * Get the doc string for this method.
	 *
	 * This can take several forms:
	 *
	 * - {@code Description of function}: A basic description with no types
	 * - {@code function(arg1:type [, optionArg:type]):returnType -- Description of function}: Function with return type
	 * - {@code function(arg1:type [, optionArg:type])->ret1:type [,optionRet1:type] -- Description of function}: Function with named return values
	 *
	 * Standard argument types are any, nil, string, number, integer, boolean and table.
	 *
	 * The function description can be multiple lines. The first line or sentence is read as a synopsis, with everything else being
	 * considered additional detail.
	 *
	 * @return The doc string.
	 */
	@Nonnull
	String getDocString();

	/**
	 * Get the priority of this provider
	 *
	 * {@link Integer#MIN_VALUE} is the lowest priority and {@link Integer#MAX_VALUE} is the highest. Providers
	 * with higher priorities will be preferred.
	 *
	 * @return The provider's priority
	 */
	default int getPriority() {
		return 0;
	}

	/**
	 * Check if this function can be applied in the given context.
	 *
	 * @param context The context to check in
	 * @return If this function can be applied.
	 * @see IContext#hasContext(Class)
	 */
	default boolean canApply(@Nonnull IPartialContext<T> context) {
		return true;
	}

	/**
	 * Apply the method
	 *
	 * @param context The context to apply within
	 * @param args    The arguments this function was called with
	 * @return The return values
	 * @throws LuaException     On the event of an error
	 * @throws RuntimeException Unhandled errors: these will be rethrown as {@link LuaException}s and the call stack logged.
	 * @see dan200.computercraft.api.lua.ILuaObject#callMethod(ILuaContext, int, Object[])
	 */
	@Nonnull
	MethodResult apply(@Nonnull IUnbakedContext<T> context, @Nonnull Object[] args) throws LuaException;


	/**
	 * Get a unique identifier for this method
	 *
	 * @return This method's unique identifier. This is only used within config files, to establish the base cost.
	 */
	@Nonnull
	default String getId() {
		return getClass().getName();
	}

	/**
	 * See if this method implements an interface or class.
	 *
	 * This is used to see if a marker interface is present.
	 *
	 * @param iface The interface or class to check
	 * @return If any method implements this interface (or extends this class)
	 * @see IMethodCollection#has(Class)
	 */
	default boolean has(@Nonnull Class<?> iface) {
		return iface.isInstance(this);
	}

	/**
	 * A delegate for some {@link IMethod}.
	 *
	 * @param <T> The type of this delegate's target.
	 */
	interface Delegate<T> {
		/**
		 * Apply this method delegate method
		 *
		 * @param context The context to apply within
		 * @param args    The arguments this function was called with
		 * @return The return values
		 * @throws LuaException     On the event of an error
		 * @throws RuntimeException Unhandled errors: these will be rethrown as {@link LuaException}s and the call stack logged.
		 * @see IMethod#apply(IUnbakedContext, Object[])
		 */
		MethodResult apply(IUnbakedContext<T> context, Object[] args) throws LuaException;
	}
}
