package com.example.composeuiadaption.analyzer

import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtNamedFunction

object CommonAnalyzeFunctions {

    fun KtNamedFunction.hasAnnotation(annotation: String): Boolean {
        return this.annotationEntries.any { it.shortName?.asString() == annotation }
    }

    inline fun KtBlockExpression.hasChildMatching(predicate: (KtElement) -> Boolean): Boolean {
        return node.getChildren(null).any { child ->
            val psi = child.psi
            if (psi is KtElement && predicate(psi)) {
                return true
            }
            false
        }
    }
}