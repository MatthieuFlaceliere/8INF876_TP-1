export class IMCServices {
  public static calculate(weight: number, height: number): number {
    let imc = weight / (height * height)
    imc = +imc.toFixed(2)
    return imc
  }
}
