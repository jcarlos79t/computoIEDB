import androidx.compose.ui.graphics.Color
import kotlinx.datetime.LocalDateTime
import org.jct.iedbs1.models.EstadoEleccion

object Utils {
    fun formatFecha(fecha: LocalDateTime): String {
        val meses = listOf(
            "Enero", "Febrero", "Marzo", "Abril", "Mayo", "Junio",
            "Julio", "Agosto", "Septiembre", "Octubre", "Noviembre", "Diciembre"
        )

        val mes = meses[fecha.monthNumber - 1]
        val dia = fecha.dayOfMonth
        val año = fecha.year

        val hora = fecha.hour.toString().padStart(2, '0')
        val minuto = fecha.minute.toString().padStart(2, '0')

        return "$mes $dia, $año - $hora:$minuto"
    }

    fun getColorEstado(estado: String): Color {
        return when (estado) {
            "PENDIENTE" -> Color(0xFF919191)
            "ENPROGRESO" -> Color(0xFF2196F3)
            "FINALIZADO" -> Color(0xFF4CAF50)
            else -> Color(0xFFF5A623)
        }
    }
    fun getEstado(estado: String): String {
        return when (estado) {
            "PENDIENTE" -> "Pendiente"
            "ENPROGRESO" -> "En progreso"
            "FINALIZADO" -> "Finalizado"
            else -> ""
        }
    }

}