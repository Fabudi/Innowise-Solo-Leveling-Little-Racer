package inc.fabudi.littleracer

import android.graphics.Path

object PathParser {
    fun parseStringIntoPathObject(pathString: String): Path {
        val parts = pathString.split(" ")
        val path = Path()
        var i = 0
        while (i < parts.size) {
            when (parts[i]) {
                "M" -> {
                    path.moveTo(parts[i + 1].toFloat(), parts[i + 2].toFloat())
                    i += 2
                }

                "m" -> {
                    path.rMoveTo(parts[i + 1].toFloat(), parts[i + 2].toFloat())
                    i += 2
                }

                "L" -> {
                    path.lineTo(parts[i + 1].toFloat(), parts[i + 2].toFloat())
                    i += 2
                }

                "l" -> {
                    path.rLineTo(parts[i + 1].toFloat(), parts[i + 2].toFloat())
                    i += 2
                }

                "C" -> {
                    path.cubicTo(
                        parts[i + 1].toFloat(),
                        parts[i + 2].toFloat(),
                        parts[i + 3].toFloat(),
                        parts[i + 4].toFloat(),
                        parts[i + 5].toFloat(),
                        parts[i + 6].toFloat()
                    )
                    i += 6
                }

                "c" -> {
                    path.rCubicTo(
                        parts[i + 1].toFloat(),
                        parts[i + 2].toFloat(),
                        parts[i + 3].toFloat(),
                        parts[i + 4].toFloat(),
                        parts[i + 5].toFloat(),
                        parts[i + 6].toFloat()
                    )
                    i += 6
                }

                "Q" -> {
                    path.quadTo(
                        parts[i + 1].toFloat(),
                        parts[i + 2].toFloat(),
                        parts[i + 3].toFloat(),
                        parts[i + 4].toFloat()
                    )
                    i += 4
                }

                "q" -> {
                    path.rQuadTo(
                        parts[i + 1].toFloat(),
                        parts[i + 2].toFloat(),
                        parts[i + 3].toFloat(),
                        parts[i + 4].toFloat()
                    )
                    i += 4
                }

                "Z" -> {
                    path.close()
                    break
                }
            }
            ++i
        }
        return path
    }
}
