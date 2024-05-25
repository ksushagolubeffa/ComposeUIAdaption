package com.example.composeuiadaption.analyzer

import com.example.composeuiadaption.analyzer.CommonAnalyzeFunctions.hasAnnotation
import org.jetbrains.kotlin.psi.*


class ComposeFunAnalyzer(private val ktClass: KtClass) {

    private val composableFunctions: List<KtNamedFunction> by lazy {
        ktClass.declarations.filterIsInstance<KtNamedFunction>().filter {
            it.hasAnnotation(COMPOSE_ANNOTATION)
        }
    }

    val hasList: Boolean by lazy {
        composableFunctions.any {
            it.hasComposableParameterOfType("androidx.compose.foundation.lazy.LazyColumn")
        }
    }

    val hasBottomNavView: Boolean by lazy {
        composableFunctions.any {
            it.hasComposableParameterOfType("androidx.compose.material.BottomNavigation")
        }
    }

    private fun KtNamedFunction.getBody(): KtBlockExpression? {
        return bodyExpression as? KtBlockExpression
    }

    val hasCardWithTextView: Boolean by lazy {
        composableFunctions.any { function ->
            if (function.hasComposableParameterOfType("androidx.compose.material.Card")) {
                function.getBody()?.let { body ->
                    body.statements.any { statement ->
                        statement is KtNamedFunction &&
                                statement.name == "Text" &&
                                statement.hasComposableParameterOfType("androidx.compose.ui.text.Text")
                    }
                } ?: false
            } else {
                false
            }
        }
    }

    private fun KtNamedFunction.hasComposableParameterOfType(type: String): Boolean {
        return valueParameters.any { it.name != null && it.name!! == "content" && it.typeReference?.text == type }
    }

    companion object {
        private const val COMPOSE_ANNOTATION = "androidx.compose.runtime.Composable"
    }
}

