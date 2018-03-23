/**
 * Esta clase servirá de ayuda de forma general en la aplicación.
 * El objetivo principal es la mejora en la calidad de código y la disminución de éste.
 */
export class EcitUtils {

    /**
     * Este método devuelve un boolean que representa el estado del parámetro recibido.
     * (null o undefined o vacío) implica true
     *
     * @param value
     */
    public static isNullOrBlank(value: any): boolean {
        return (value === null) || (value === undefined) || (value === '') || (value === 'undefined');
    }

    /**
     * Este método devuelve un boolean que representa el estado del array recibido.
     * (null o undefined o vacío) implica true
     *
     * @param value
     */
    public static isNullOrEmpty(value: any): boolean {
        return (!Array.isArray(value)) || (!value.length);
    }

    /**
	 * Devuelve un String que contiene los elementos del array separados por el contenido del parámetro `separator` (por defecto un espacio), validando cada campo antes de concatenar.
	 *
	 * @param usuario
     * @param separador (Por defecto un espacio)
	 * @return
	 */
    public static concatenarStrings(params: string[], separator = ' '): string {
        return params.filter((o) => o).join(separator);
    }

    /**
     * Devuelve el string pasado por parámetro sin acentos.
     *
     * @param value
     */
    public static removeDiacritics(value: string): string {
        return value ? value.normalize('NFD').replace(/[\u0300-\u036f]/g, '') : '';
    }
}
