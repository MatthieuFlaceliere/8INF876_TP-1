import Imc from "#models/imc"

export class IMCServices {
  public static async calculateAndSave(weight: number, height: number): Promise<Imc> {
    let imc = weight / (height * height)
    imc = +imc.toFixed(2)

    // Enregistrer dans la base de données
    const imcRecord = await Imc.create({ weight, imc })

    return imcRecord
  }
  public static async getAllImcRecords(): Promise<Imc[]> {
    return await Imc.all()
  }
}