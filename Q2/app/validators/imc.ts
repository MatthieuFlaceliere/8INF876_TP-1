import vine from '@vinejs/vine'

export const imcValidator = vine.compile(
  vine.object({
    weight: vine.number().positive(),
    height: vine.number().positive(),
  })
)
